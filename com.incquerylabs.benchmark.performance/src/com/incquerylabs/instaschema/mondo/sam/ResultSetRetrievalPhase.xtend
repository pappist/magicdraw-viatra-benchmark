package com.incquerylabs.instaschema.mondo.sam

import eu.mondo.sam.core.DataToken
import eu.mondo.sam.core.metrics.ScalarMetric
import eu.mondo.sam.core.metrics.TimeMetric
import eu.mondo.sam.core.phases.AtomicPhase
import eu.mondo.sam.core.results.PhaseResult
import org.apache.log4j.Logger
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchBackend
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchBackendFactory
import org.eclipse.viatra.query.runtime.localsearch.profiler.LocalSearchProfilerAdapter

class ResultSetRetrievalPhase extends AtomicPhase {
	
	new(String phaseName) {
		super(phaseName)
	}
	
	override execute(DataToken token, PhaseResult phaseResult) {
//		val logger = Logger.getLogger("com.incquerylabs.magicdraw.benchmark");
		val myToken = token as MyDataToken

		// Time and memory are measured
		val timer = new TimeMetric("Time")
		
//		val queryBackend = myToken.engine.getQueryBackend(LocalSearchBackendFactory.INSTANCE)
//		val profiler = new LocalSearchProfilerAdapter()
//		if(queryBackend instanceof LocalSearchBackend){
//			queryBackend.addAdapter(profiler)	
//		}
		
		
		timer.startMeasure
				
		val matches = myToken.matcher.allMatches
		
		timer.stopMeasure
		
		val matchSetSize = new ScalarMetric("MatchSetSize")
		matchSetSize.value = matches.size
		
		phaseResult.addMetrics(timer, matchSetSize)
		
//		if(queryBackend instanceof LocalSearchBackend){
//			logger.info(profiler)
//		}		
	}	
	
}