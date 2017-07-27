package com.incquerylabs.instaschema.mondo.sam

import com.incquerylabs.instaschema.performance.EngineImpl
import eu.mondo.sam.core.DataToken
import eu.mondo.sam.core.phases.AtomicPhase
import eu.mondo.sam.core.results.PhaseResult
import java.util.Collection
import org.eclipse.viatra.query.runtime.api.IQuerySpecification

/**
 * Creates the Viatra query engine in the DataToken based on the arguments received in its ctor.
 */
class InitPhase extends AtomicPhase {
	
	private Collection<IQuerySpecification<?>> hintedQueries	
	private EngineImpl engineImpl
	
	new(String phaseName, EngineImpl engineImpl, Collection<IQuerySpecification<?>> hintedQueries) {
		super(phaseName)
		this.engineImpl = engineImpl
		this.hintedQueries = hintedQueries
	}
	
	override execute(DataToken token, PhaseResult phaseResult) {
		val MyDataToken dataToken = token as MyDataToken
		dataToken.initEngine(engineImpl, hintedQueries)  
	}
	
}