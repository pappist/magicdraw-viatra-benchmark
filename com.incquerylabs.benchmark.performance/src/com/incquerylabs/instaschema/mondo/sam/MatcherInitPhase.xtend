package com.incquerylabs.instaschema.mondo.sam

import com.google.common.collect.ImmutableSet
import eu.mondo.sam.core.DataToken
import eu.mondo.sam.core.metrics.MemoryMetric
import eu.mondo.sam.core.metrics.ScalarMetric
import eu.mondo.sam.core.metrics.TimeMetric
import eu.mondo.sam.core.phases.AtomicPhase
import eu.mondo.sam.core.results.PhaseResult
import java.util.HashSet
import java.util.Set
import java.util.concurrent.Callable
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine
import org.eclipse.viatra.query.runtime.api.IQuerySpecification
import org.eclipse.viatra.query.runtime.base.api.IndexingLevel
import org.eclipse.viatra.query.runtime.emf.EMFScope
import org.eclipse.viatra.query.runtime.emf.types.EClassTransitiveInstancesKey
import org.eclipse.viatra.query.runtime.emf.types.EClassUnscopedTransitiveInstancesKey
import org.eclipse.viatra.query.runtime.emf.types.EDataTypeInSlotsKey
import org.eclipse.viatra.query.runtime.emf.types.EStructuralFeatureInstancesKey
import org.eclipse.viatra.query.runtime.matchers.context.IInputKey
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody
import org.eclipse.viatra.query.runtime.matchers.psystem.PConstraint
import org.eclipse.viatra.query.runtime.matchers.psystem.basicenumerables.TypeConstraint
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery
import org.eclipse.viatra.query.runtime.util.ViatraQueryLoggingUtil

class MatcherInitPhase extends AtomicPhase {
	
	private IQuerySpecification<?> querySpecification
	private boolean setIndex
	
	new(String phaseName, IQuerySpecification<?> querySpecification, boolean setIndex) {
		super(phaseName)
		this.querySpecification = querySpecification
		this.setIndex = setIndex		
	}
	
	override execute(DataToken token, PhaseResult phaseResult) {
		val dataToken = token as MyDataToken

		// Time and memory are measured
		val timer = new TimeMetric("Time")
		val prememory = new MemoryMetric("PreMemory")
		val memory = new MemoryMetric("Memory")
		prememory.measure
		
		ViatraQueryLoggingUtil.defaultLogger. info("Measuring " + querySpecification.name + "...")
		println("Measuring " + querySpecification.name + "...")
		timer.startMeasure
		// Setting the indexes if needed  (decreases traversal time in case of local search)
		if (setIndex) {
			val indexingTimer = new TimeMetric("IndexingTime")
			indexingTimer.startMeasure
			this.setIndex(querySpecification, dataToken.engine)
			indexingTimer.stopMeasure
			phaseResult.addMetrics(indexingTimer)
		}
		
				
		dataToken.matcher = querySpecification.getMatcher(dataToken.engine)
		
		timer.stopMeasure
		memory.measure
		
		val memoryDelta = new ScalarMetric("MemoryDelta")
		
        memoryDelta.value = Math.max(Long.parseLong(memory.value)-Long.parseLong(prememory.value), 1)
        
		phaseResult.addMetrics(timer, prememory, memory, memoryDelta)
	}
	
	private def getName(IQuerySpecification<?> querySpecification) {
		querySpecification.getFullyQualifiedName().substring(querySpecification.getFullyQualifiedName().lastIndexOf(".") + 1)
	}
	
	/**
	 * Registers TypeConstraint to the "UnderlyingEMFIndex" which can accelerate the local search traversal time.
	 */
	private def void setIndex(IQuerySpecification<?> specification, AdvancedViatraQueryEngine engine) {
		val index = EMFScope.extractUnderlyingEMFIndex(engine);
		index.coalesceTraversals(new Callable<Void>() {
			override call() throws Exception {
				val Set<EClass> classes = new HashSet<EClass>();
				val Set<EDataType> dataTypes = new HashSet<EDataType>();
				val Set<EStructuralFeature> features = new HashSet<EStructuralFeature>();
				
				val queries =  ImmutableSet.builder.addAll(specification.getInternalQueryRepresentation().getAllReferredQueries()).add(specification.internalQueryRepresentation).build
				for (PQuery query : queries) {
					for (PBody body : query.getDisjunctBodies().getBodies()) {
			            for (PConstraint constraint : body.getConstraints()) {
			                if (constraint instanceof TypeConstraint) {
			                    val IInputKey supplierKey = (constraint as TypeConstraint).getSupplierKey();
			                    if (supplierKey instanceof EClassTransitiveInstancesKey) {
			                    	val eClass = (supplierKey as EClassTransitiveInstancesKey).getEmfKey();
			                    	classes.add(eClass);
			                    }
			                    else if (supplierKey instanceof EClassUnscopedTransitiveInstancesKey) {
			                    	val eClass = (supplierKey as EClassUnscopedTransitiveInstancesKey).getEmfKey();
			                    	classes.add(eClass);
			                    }
			                    else if (supplierKey instanceof EDataTypeInSlotsKey) {
			                    	val eDataType = (supplierKey as EDataTypeInSlotsKey).getEmfKey();
			                    	dataTypes.add(eDataType);
			                    }
			                    else if (supplierKey instanceof EStructuralFeatureInstancesKey) {
			                    	val eStructuralFeature = (supplierKey as EStructuralFeatureInstancesKey).getEmfKey();
			                    	features.add(eStructuralFeature);
			                    }
			                }
			            }
			        }
				}
				index.registerEClasses(classes, IndexingLevel.FULL);
				index.registerEDataTypes(dataTypes, IndexingLevel.FULL);
				index.registerEStructuralFeatures(features, IndexingLevel.FULL);
				// TODO this should not be needed after fixing this in LocalSearchResultProvider.prepare()
				index.setWildcardLevel(IndexingLevel.STATISTICS);
				return null;
			}			
		});
	}
	
	
}