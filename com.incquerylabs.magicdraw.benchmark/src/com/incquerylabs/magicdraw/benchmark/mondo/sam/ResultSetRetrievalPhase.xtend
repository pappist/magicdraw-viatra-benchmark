package com.incquerylabs.magicdraw.benchmark.mondo.sam

import eu.mondo.sam.core.DataToken
import eu.mondo.sam.core.metrics.ScalarMetric
import eu.mondo.sam.core.metrics.TimeMetric
import eu.mondo.sam.core.phases.AtomicPhase
import eu.mondo.sam.core.results.PhaseResult
import org.apache.log4j.Logger
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchBackend
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchGenericBackendFactory
import org.eclipse.viatra.query.runtime.localsearch.profiler.LocalSearchProfilerAdapter

class ResultSetRetrievalPhase extends AtomicPhase {

	new(String phaseName) {
		super(phaseName)
	}

	override execute(DataToken token, PhaseResult phaseResult) {
		val logger = Logger.getLogger("com.incquerylabs.magicdraw.benchmark");
		val myToken = token as QueryMatcherToken

		// Time and memory are measured
		val timer = new TimeMetric("Time")

		val queryBackend = myToken.engine.getQueryBackend(LocalSearchGenericBackendFactory.INSTANCE)
		val profiler = new LocalSearchProfilerAdapter()
		if(queryBackend instanceof LocalSearchBackend){
			queryBackend.addAdapter(profiler)	
		}
		timer.startMeasure

		val matches = myToken.matcher.countMatches

		timer.stopMeasure

		val matchSetSize = new ScalarMetric("MatchSetSize")
		matchSetSize.value = matches

		phaseResult.addMetrics(timer, matchSetSize)

		if(queryBackend instanceof LocalSearchBackend){
			logger.info(profiler)
} 		
	}

}
