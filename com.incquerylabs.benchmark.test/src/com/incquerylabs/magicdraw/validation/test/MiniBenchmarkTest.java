package com.incquerylabs.magicdraw.validation.test;

import java.util.Collection;
import java.util.stream.Collectors;

public class MiniBenchmarkTest extends MondoSamTest {

    protected Collection<IQuerySpecification<?>> getReteQuerySpecifications() {
        
        final ImmutableSet<IQuerySpecification<?>> queries = ImmutableSet.<IQuerySpecification<?>>builder()
                .add(APerformanceQueries.instance().getTransitiveSubstatesWithCheck3())
                .build();
        return queries;
    }
    
    @Override
    protected Collection<IQuerySpecification<?>> getLSQuerySpecifications() {
        return getReteQuerySpecifications();
    }
    
    @Override
    protected Collection<IQuerySpecification<?>> getHybridQuerySpecifications() {
        final ImmutableSet<IQuerySpecification<?>> queries = ImmutableSet.<IQuerySpecification<?>>builder()
                .add(IncrementalQueries.instance().getTransitiveSubstatesWithCheck3())
                .build();
        return queries;
    }
}
