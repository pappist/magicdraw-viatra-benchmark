package com.incquerylabs.instaschema.mondo.sam

import com.incquerylabs.instaschema.performance.EngineImpl
import com.nomagic.magicdraw.core.Application
import java.util.Collection
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine
import org.eclipse.viatra.query.runtime.api.IQuerySpecification
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions
import org.eclipse.viatra.query.runtime.base.api.BaseIndexOptions
import org.eclipse.viatra.query.runtime.base.api.filters.IBaseIndexFeatureFilter
import org.eclipse.viatra.query.runtime.emf.EMFScope
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchBackendFactory
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchHints
import org.eclipse.viatra.query.runtime.localsearch.planner.cost.IConstraintEvaluationContext
import org.eclipse.viatra.query.runtime.localsearch.planner.cost.impl.IndexerBasedConstraintCostFunction
import org.eclipse.viatra.query.runtime.matchers.psystem.IQueryReference
import org.eclipse.viatra.query.runtime.matchers.psystem.PConstraint

/**
 * Responsible for creating Viatra query engines with different settings: Rete, local search, hinted local search.
 */
class EngineCreator {
	
	private static EngineCreator instance = new EngineCreator
	
	def static getInstance() {
		return instance
	}
	
	def createEngine(EngineImpl engineImpl, Collection<IQuerySpecification<?>> queries)  {
		if (queries != null && !queries.empty) {
			return createHintedLocalSearchEngine(queries)
		}
		switch (engineImpl) {
		case RETE:
			return createReteEngine			
		case LOCAL_SEARCH:
			return createLocalSearchEngine 
		default:
			throw new IllegalArgumentException("No such engine: " + engineImpl) 
		}
	}

	private def createReteEngine() throws ViatraQueryException {
		val baseIndexOptions = new BaseIndexOptions().withFeatureFilterConfiguration(new IBaseIndexFeatureFilter() {
					override boolean isFiltered(EStructuralFeature arg0) {
						if (arg0 instanceof EReference && (arg0 as EReference).isContainment()) {
							return arg0.getName().contains("_from_");
						}
						return false;
					}
				}).withStrictNotificationMode(false);
		val engine = AdvancedViatraQueryEngine.createUnmanagedEngine(
				new EMFScope(Application.getInstance().getProject().getModel(), baseIndexOptions));
		return engine;
	}

	private def createLocalSearchEngine() throws ViatraQueryException {
		val baseIndexOptions = new BaseIndexOptions().withFeatureFilterConfiguration(new IBaseIndexFeatureFilter() {
					override boolean isFiltered(EStructuralFeature arg0) {
						if (arg0 instanceof EReference && (arg0 as EReference).isContainment()) {
							return arg0.getName().contains("_from_");
						}
						return false;
					}
				}).withStrictNotificationMode(false);
		val engineOptions = ViatraQueryEngineOptions.defineOptions()
				.withDefaultBackend(LocalSearchBackendFactory.INSTANCE).build();
		val engine = AdvancedViatraQueryEngine.createUnmanagedEngine(
				new EMFScope(Application.getInstance().getProject().getModel(), baseIndexOptions), engineOptions);
		return engine;
	}
	
	private def createHintedLocalSearchEngine(Collection<IQuerySpecification<?>> patternNames) throws ViatraQueryException {
		val baseIndexOptions = new BaseIndexOptions().withFeatureFilterConfiguration(new IBaseIndexFeatureFilter() {
					override boolean isFiltered(EStructuralFeature arg0) {
						if (arg0 instanceof EReference && (arg0 as EReference).isContainment()) {
							return arg0.getName().contains("_from_");
						}
						return false;
					}
				}).withStrictNotificationMode(false);
		val hints = getHints(patternNames);
		val engineOptions = ViatraQueryEngineOptions.defineOptions()
				.withDefaultBackend(LocalSearchBackendFactory.INSTANCE).withDefaultHint(hints).build();
		val engine = AdvancedViatraQueryEngine.createUnmanagedEngine(
				new EMFScope(Application.getInstance().getProject().getModel(), baseIndexOptions), engineOptions);
	
		return engine;
	}

	private def getHints(Collection<IQuerySpecification<?>> queries) {
		return LocalSearchHints.getDefault().setCostFunction(new IndexerBasedConstraintCostFunction() {
			override double calculateCost(PConstraint constraint, IConstraintEvaluationContext input) {
				try {
				if (constraint instanceof IQueryReference) {
					val patternName = (constraint as IQueryReference).getReferredQuery().getFullyQualifiedName();
					if (queries.filter[it.getFullyQualifiedName().equals(patternName)].length > 0) {
						return 1.0d; 
					}
				}
				return super.calculateCost(constraint, input);
				} catch (Exception e) { e.printStackTrace(); return 0;}
			}
		}).build();
	}
	
}