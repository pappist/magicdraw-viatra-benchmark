package com.incquerylabs.magicdraw.benchmark.mondo.sam

import eu.mondo.sam.core.DataToken
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine
import org.eclipse.viatra.query.runtime.api.IPatternMatch
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher
import org.eclipse.viatra.query.runtime.base.api.BaseIndexOptions
import org.eclipse.viatra.query.runtime.base.api.filters.IBaseIndexFeatureFilter
import org.eclipse.viatra.query.runtime.emf.EMFScope
import org.eclipse.viatra.query.runtime.matchers.backend.QueryEvaluationHint
import com.nomagic.magicdraw.core.Application
import org.eclipse.viatra.query.runtime.base.api.IndexingLevel

/**
 * Contains an AdvancedViatraQueryEngine and a ViatraQueryMatcher.
 */
class QueryMatcherToken implements DataToken {
	
	private AdvancedViatraQueryEngine engine	
	private ViatraQueryMatcher<? extends IPatternMatch> matcher
	
	def initEngine(QueryEvaluationHint engineDefaultHint) {
		if (engine !== null) {
			engine.dispose
		}
		this.engine = createEngine(engineDefaultHint)		
	}
	
	def createEngine(QueryEvaluationHint engineDefaultHint)  {
		val baseIndexOptions = new BaseIndexOptions().withFeatureFilterConfiguration(new IBaseIndexFeatureFilter() {
					override boolean isFiltered(EStructuralFeature arg0) {
						if (arg0 instanceof EReference && (arg0 as EReference).isContainment()) {
							return arg0.getName().contains("_from_");
						}
						return false;
					}
				}).withStrictNotificationMode(false).withWildcardLevel(IndexingLevel.FULL);
				
		if (engineDefaultHint !== null) {
			val engineOptions = ViatraQueryEngineOptions.defineOptions().withDefaultHint(engineDefaultHint).build();
			
			return AdvancedViatraQueryEngine.createUnmanagedEngine(
					new EMFScope(Application.getInstance().getProject().getModel(), baseIndexOptions), engineOptions);			
		} else {
			return AdvancedViatraQueryEngine.createUnmanagedEngine(
					new EMFScope(Application.getInstance().getProject().getModel(), baseIndexOptions));
		} 
	}
	
	def getEngine() {
		return engine
	}
	
	def setMatcher(ViatraQueryMatcher<? extends IPatternMatch> matcher) {
		this.matcher = matcher
	}
	
	def getMatcher() {
		return matcher
	}
	
	override init() {
		// no op
	}
	
	override destroy() {
		if (engine !== null) {
			engine.dispose
		}
	}	
	
}