package com.incquerylabs.magicdraw.benchmark.mondo.sam

import eu.mondo.sam.core.DataToken
import eu.mondo.sam.core.phases.AtomicPhase
import eu.mondo.sam.core.results.PhaseResult
import org.eclipse.viatra.query.runtime.matchers.backend.QueryEvaluationHint

/**
 * Creates the Viatra query engine in the DataToken based on the arguments received in its ctor.
 */
class InitPhase extends AtomicPhase {
	
	private QueryEvaluationHint engineDefaultHint
	
	new(String phaseName, QueryEvaluationHint engineDefaultHint) {
		super(phaseName)
		this.engineDefaultHint = engineDefaultHint
	}
	
	override execute(DataToken token, PhaseResult phaseResult) {
		val QueryMatcherToken dataToken = token as QueryMatcherToken
		dataToken.initEngine(engineDefaultHint)  
	}
	
}