package com.incquerylabs.instaschema.mondo.sam

import com.incquerylabs.instaschema.performance.EngineImpl
import eu.mondo.sam.core.phases.SequencePhase
import eu.mondo.sam.core.results.CaseDescriptor
import eu.mondo.sam.core.scenarios.BenchmarkScenario
import org.eclipse.viatra.query.runtime.api.IQuerySpecification
import org.eclipse.viatra.query.runtime.matchers.backend.QueryEvaluationHint

class SingleQueryScenario extends BenchmarkScenario {
	
	private IQuerySpecification<?> querySpecification
	private EngineImpl engineImpl
	private String caseName
	private boolean setIndex
	private QueryEvaluationHint engineDefaultHints
	
	new(String caseName, IQuerySpecification<?> querySpecification, EngineImpl engineImpl, QueryEvaluationHint engineDefaultHints, boolean setIndex, int runIndex, String toolName) {
		this.caseName = caseName
		this.querySpecification = querySpecification
		this.engineImpl = engineImpl
		this.engineDefaultHints = engineDefaultHints
		this.setIndex = setIndex
		this.runIndex = runIndex
		this.tool = toolName;
	}
		
	override build() {
		// This phase will contain a series of phases each responsible for a matcher init/result set retrieval measurement
		val rootPhase = new SequencePhase
		// In case of local search, the traversal time can be reduced 
		// Local search has to be set, and the user must explicitly give a boolean flag (setIndex in ctor) set to true
		val indexNeedsSetting = (engineImpl == EngineImpl.LOCAL_SEARCH && this.setIndex)
		
		// A phase responsible for a matcher init/result set retrieval measurement
		val measurePhase = new SequencePhase
		measurePhase.addPhases(
			new InitPhase(querySpecification.name + "InitPhase", engineDefaultHints),
			new MatcherInitPhase("MatcherInitPhase", querySpecification, indexNeedsSetting), 
			new ResultSetRetrievalPhase("ResultSetRetrievalPhase")
		)
		// Adding it to the root phase
		rootPhase.addPhases(measurePhase)		
		
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
		return "SingleQueryScenario"
	}
	
	/**
	 * Returns the measurement setting (Rete, local search, etc.) based on the fields of the object. 
	 */
	private def getToolName() {
		return tool;	
	}
	
	/**
	 * Returns the simple (not fully qualified) name of a query.
	 */
	private def getName(IQuerySpecification<?> querySpecification) {
		querySpecification.getFullyQualifiedName().substring(querySpecification.getFullyQualifiedName().lastIndexOf(".") + 1)
	}
	
}