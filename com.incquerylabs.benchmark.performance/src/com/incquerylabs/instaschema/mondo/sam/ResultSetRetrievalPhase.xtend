package com.incquerylabs.instaschema.mondo.sam

import eu.mondo.sam.core.DataToken
import eu.mondo.sam.core.metrics.TimeMetric
import eu.mondo.sam.core.phases.AtomicPhase
import eu.mondo.sam.core.results.PhaseResult
import eu.mondo.sam.core.metrics.MemoryMetric
import eu.mondo.sam.core.metrics.ScalarMetric

class ResultSetRetrievalPhase extends AtomicPhase {
	
	new(String phaseName) {
		super(phaseName)
	}
	
	override execute(DataToken token, PhaseResult phaseResult) {
		val myToken = token as MyDataToken

		// Time and memory are measured
		val timer = new TimeMetric("Time")
		
		timer.startMeasure
				
		val matches = myToken.matcher.allMatches
		
		timer.stopMeasure
		
		val matchSetSize = new ScalarMetric("MatchSetSize")
		matchSetSize.value = matches.size
		
		phaseResult.addMetrics(timer, matchSetSize)		
	}	
	
}