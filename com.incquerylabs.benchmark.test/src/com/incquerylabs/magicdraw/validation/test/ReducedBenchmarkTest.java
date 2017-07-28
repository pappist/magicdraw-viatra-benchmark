package com.incquerylabs.magicdraw.validation.test;

import java.util.Collection;

import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;

import com.google.common.collect.ImmutableSet;
import com.incquerylabs.instaschema.performance.incrementalqueries.IncrementalQueries;
import com.incquerylabs.instaschema.performance.queries.APerformanceQueries;

public class ReducedBenchmarkTest extends MondoSamTest {

    protected Collection<IQuerySpecification<?>> getReteQuerySpecifications() throws ViatraQueryException {
        
        final ImmutableSet<IQuerySpecification<?>> queries = ImmutableSet.<IQuerySpecification<?>>builder()
                .add(APerformanceQueries.instance().getBlocksOrRequirementsOrConstraints())
                .add(APerformanceQueries.instance().getAlphabeticalDependencies())
                .add(APerformanceQueries.instance().getCircularDependencies())
                .add(APerformanceQueries.instance().getLoopTransitionWithTriggerEffectEventNoGuard())
                .add(APerformanceQueries.instance().getStateWithMostSubstates())
                .add(APerformanceQueries.instance().getTransitiveSubstatesWithCheck3())
                .build();
        return queries;
    }
    
    @Override
    protected Collection<IQuerySpecification<?>> getLSQuerySpecifications() throws ViatraQueryException {
        return getReteQuerySpecifications();
    }
    
    @Override
    protected Collection<IQuerySpecification<?>> getHybridQuerySpecifications() throws ViatraQueryException {
        final ImmutableSet<IQuerySpecification<?>> queries = ImmutableSet.<IQuerySpecification<?>>builder()
                .add(IncrementalQueries.instance().getTransitiveSubstatesWithCheck3())
                .build();
        return queries;
    }
    
    @Override
    protected Integer[] getModelSizes() {
        return new Integer[]{300000, 540000, 780000};
    }
    
}
