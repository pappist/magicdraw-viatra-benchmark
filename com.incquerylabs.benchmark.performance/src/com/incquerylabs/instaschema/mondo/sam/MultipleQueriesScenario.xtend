package com.incquerylabs.instaschema.mondo.sam

import com.incquerylabs.instaschema.performance.EngineImpl
import com.incquerylabs.instaschema.performance.incrementalqueries.util.ParentStateQuerySpecification
import com.incquerylabs.instaschema.performance.incrementalqueries.util.TransitiveSubstatesWithCheck2QuerySpecification
import com.incquerylabs.instaschema.performance.incrementalqueries.util.TransitiveSubstatesWithCheck3QuerySpecification
import com.incquerylabs.instaschema.performance.incrementalqueries.util.TransitiveSubstatesWithCheckQuerySpecification
import eu.mondo.sam.core.phases.SequencePhase
import eu.mondo.sam.core.results.CaseDescriptor
import eu.mondo.sam.core.scenarios.BenchmarkScenario
import java.util.Collection
import org.eclipse.viatra.query.runtime.api.IQuerySpecification

class MultipleQueriesScenario extends BenchmarkScenario {
	
	private Collection<IQuerySpecification<?>> querySpecifications
	private EngineImpl engineImpl
	private Collection<IQuerySpecification<?>> hintedQueries
	private String caseName
	private boolean setIndex
	
	new(String caseName, Collection<IQuerySpecification<?>> querySpecifications, EngineImpl engineImpl, Collection<IQuerySpecification<?>> hintedQueries, boolean setIndex, int runIndex, String toolName) {
		this.caseName = caseName
		this.querySpecifications = querySpecifications
		this.engineImpl = engineImpl
		this.hintedQueries = hintedQueries
		this.setIndex = setIndex
		this.runIndex = runIndex
		this.tool = toolName;
	}
	
	new(String caseName, Collection<IQuerySpecification<?>> querySpecifications, EngineImpl engineImpl, Collection<IQuerySpecification<?>> hintedQueries, boolean setIndex, int runIndex) {
		this.caseName = caseName
		this.querySpecifications = querySpecifications
		this.engineImpl = engineImpl
		this.hintedQueries = hintedQueries
		this.setIndex = setIndex
		this.runIndex = runIndex
	}
		
	override build() {
		// This phase will contain a series of phases each responsible for a matcher init/result set retrieval measurement
		val rootPhase = new SequencePhase
		// In case of local search, the traversal time can be reduced 
		// Local search has to be set, and the user must explicitly give a boolean flag (setIndex in ctor) set to true
		val indexNeedsSetting = (engineImpl == EngineImpl.LOCAL_SEARCH && this.setIndex)
		
		for (querySpecification : querySpecifications.sortBy[it.fullyQualifiedName]) {
			// A phase responsible for a matcher init/result set retrieval measurement
			val measurePhase = new SequencePhase
			measurePhase.addPhases(
				new InitPhase(querySpecification.name + "InitPhase", this.engineImpl, this.hintedQueries),
				new MatcherInitPhase("MatcherInitPhase", querySpecification, indexNeedsSetting), 
				new ResultSetRetrievalPhase("ResultSetRetrievalPhase")
			)
			// Adding it to the root phase
			rootPhase.addPhases(measurePhase)		
		}
		
		this.rootPhase = rootPhase
	}
	
	override getCaseDescriptor() {
		val descriptor = new CaseDescriptor => [
			it.caseName = this.caseName
			it.size = this.size
			it.runIndex = this.runIndex
			it.scenario = this.getName
			it.tool = this.getToolName
		]
		return descriptor
	}
	
	def getName() {
		return "MultipleQueriesScenario"
	}
	
	/**
	 * Returns the measurement setting (Rete, local search, etc.) based on the fields of the object. 
	 */
	private def getToolName() {
		if(tool!=null){
			return tool;	
		}
		if (engineImpl == EngineImpl.LOCAL_SEARCH && (querySpecifications.contains(ParentStateQuerySpecification.instance) ||
			querySpecifications.contains(TransitiveSubstatesWithCheckQuerySpecification.instance) || 
			querySpecifications.contains(TransitiveSubstatesWithCheck2QuerySpecification.instance) ||
			querySpecifications.contains(TransitiveSubstatesWithCheck3QuerySpecification.instance))) {
			return "Hybrid"
		}
		else if (engineImpl == EngineImpl.LOCAL_SEARCH) {
			return "LocalSearch"
		}
		else {
			return "Rete"
		}
	}
	
	/**
	 * Returns the simple (not fully qualified) name of a query.
	 */
	private def getName(IQuerySpecification<?> querySpecification) {
		querySpecification.getFullyQualifiedName().substring(querySpecification.getFullyQualifiedName().lastIndexOf(".") + 1)
	}
	
}