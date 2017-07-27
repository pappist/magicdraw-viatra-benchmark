package com.incquerylabs.instaschema.mondo.sam

import com.incquerylabs.instaschema.performance.EngineImpl
import eu.mondo.sam.core.DataToken
import java.util.Collection
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine
import org.eclipse.viatra.query.runtime.api.IPatternMatch
import org.eclipse.viatra.query.runtime.api.IQuerySpecification
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher

/**
 * Contains an AdvancedViatraQueryEngine and a ViatraQueryMatcher.
 */
class MyDataToken implements DataToken {
	
	private AdvancedViatraQueryEngine engine	
	private ViatraQueryMatcher<? extends IPatternMatch> matcher
	
	def initEngine(EngineImpl engineImpl, Collection<IQuerySpecification<?>> queries) {
		if (engine != null) {
			engine.dispose
		}
		this.engine = EngineCreator.instance.createEngine(engineImpl, queries)
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
		if (engine != null) {
			engine.dispose
		}
	}	
	
}