package com.incquerylabs.magicdraw.benchmark;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Level;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.base.api.BaseIndexOptions;
import org.eclipse.viatra.query.runtime.base.api.IndexingLevel;
import org.eclipse.viatra.query.runtime.base.api.NavigationHelper;
import org.eclipse.viatra.query.runtime.base.api.filters.IBaseIndexFeatureFilter;
import org.eclipse.viatra.query.runtime.emf.EMFScope;
import org.eclipse.viatra.query.runtime.emf.types.EClassTransitiveInstancesKey;
import org.eclipse.viatra.query.runtime.emf.types.EClassUnscopedTransitiveInstancesKey;
import org.eclipse.viatra.query.runtime.emf.types.EDataTypeInSlotsKey;
import org.eclipse.viatra.query.runtime.emf.types.EStructuralFeatureInstancesKey;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchBackend;
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchBackendFactory;
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchHints;
import org.eclipse.viatra.query.runtime.localsearch.planner.cost.IConstraintEvaluationContext;
import org.eclipse.viatra.query.runtime.localsearch.planner.cost.impl.IndexerBasedConstraintCostFunction;
import org.eclipse.viatra.query.runtime.localsearch.profiler.LocalSearchProfilerAdapter;
import org.eclipse.viatra.query.runtime.matchers.backend.QueryEvaluationHint;
import org.eclipse.viatra.query.runtime.matchers.context.IInputKey;
import org.eclipse.viatra.query.runtime.matchers.psystem.IQueryReference;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.PConstraint;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicenumerables.TypeConstraint;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.query.runtime.util.ViatraQueryLoggingUtil;

import com.google.common.base.Stopwatch;
import com.incquerylabs.magicdraw.benchmark.incrementalqueries.IncrementalQueries;
import com.incquerylabs.magicdraw.benchmark.queries.APerformanceQueries;
import com.incquerylabs.magicdraw.benchmark.queries.WarmUpQueries;
import com.incquerylabs.magicdraw.benchmark.queries.util.TransitiveSubstatesWithCheck3QuerySpecification;
import com.nomagic.actions.AMConfigurator;
import com.nomagic.actions.ActionsManager;
import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;
import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.actions.MDActionsCategory;
import com.nomagic.magicdraw.commandline.CommandLineActionManager;
import com.nomagic.magicdraw.core.Application;

public class InstaSchemaPerformancePlugin extends com.nomagic.magicdraw.plugins.Plugin {

	public static boolean initialized = false;	
	
	@Override
	public void init() {
		initializeActions();
		CommandLineActionManager.getInstance().addAction(new PerformanceBenchmarkRunner());
		initialized = true; 
	}

	private void initializeActions() {
		ActionsConfiguratorsManager.getInstance().addMainToolbarConfigurator(new AMConfigurator() {

			@Override
			public int getPriority() {
				return AMConfigurator.MEDIUM_PRIORITY; 
			}

			@Override
			public void configure(ActionsManager manager) {
				MDActionsCategory category = new MDActionsCategory("", "");
				category.addAction(new MDAction("Query_Benchmark", "Query_Benchmark", null, null) {
					@SuppressWarnings("unchecked")
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							ViatraQueryLoggingUtil.getDefaultLogger().setLevel(Level.ERROR);
							final String resultFilePath = "F:\\git\\magicdraw-tools\\com.incquerylabs.instaschema.performance\\results\\rete_results.txt";
							warmUpJvm();
							List<MeasurementData> measurementData = measureReteTimeAll();
							print(measurementData, MeasurementData.getTimeCSVFields(), resultFilePath);
//							final String memoryResultFilePath = "F:\\git\\magicdraw-tools\\com.incquerylabs.instaschema.performance\\results\\rete_results_memory.txt";
//							measureReteMemoryAll(memoryResultFilePath);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
				category.addAction(new MDAction("LS Query_Benchmark", "LS Query_Benchmark", null, null) {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {							
							ViatraQueryLoggingUtil.getDefaultLogger().setLevel(Level.ERROR);
							final String resultFilePath = "F:\\git\\magicdraw-tools\\com.incquerylabs.instaschema.performance\\results\\local_results.txt";
							warmUpJvm();
							List<MeasurementData> measurementData = measureLocalSearchTimeAll();
							print(measurementData, MeasurementData.getTimeCSVFields(), resultFilePath);
//							final String memoryResultFilePath = "F:\\git\\magicdraw-tools\\com.incquerylabs.instaschema.performance\\results\\local_results_memory.txt";
//							measureLocalSearchMemoryAll(memoryResultFilePath);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}	
				});
				category.addAction(new MDAction("Hybrid", "Hybrid", null, null) {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {							
							ViatraQueryLoggingUtil.getDefaultLogger().setLevel(Level.ERROR);
							final String resultFilePath = "F:\\git\\magicdraw-tools\\com.incquerylabs.instaschema.performance\\results\\hybrid_results.txt";
							warmUpJvm();
							List<MeasurementData> measurementData = measureHybridTimeAll();
							print(measurementData, MeasurementData.getTimeCSVFields(), resultFilePath);
//							final String memoryResultFilePath = "F:\\git\\magicdraw-tools\\com.incquerylabs.instaschema.performance\\results\\local_results_memory.txt";
//							measureLocalSearchMemoryAll(memoryResultFilePath);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}	
				});
				category.addAction(new MDAction("Single Query", "Single Query", null, null) {
					@Override
					public void actionPerformed(ActionEvent e) {
						final String planFilePath = "F:\\git\\magicdraw-tools\\com.incquerylabs.instaschema.performance\\results\\single_query_plan.txt";
						try {
							ViatraQueryLoggingUtil.getDefaultLogger().setLevel(Level.ERROR);
							IQuerySpecification<?> specification = TransitiveSubstatesWithCheck3QuerySpecification.instance();
							MeasurementData measurementData = measureSingleQuery(specification, Collections.emptyList());
							print(Collections.singletonList(measurementData), MeasurementData.getTimeCSVFields(), planFilePath);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}	
				});
				manager.addCategory(category);
			}
		});

	}
	
	public List<MeasurementData> measureReteMemoryAll() throws ViatraQueryException {
		return measureMemoryAll(returnQuerySpecifications(), EngineImpl.RETE);
	}
	
	public List<MeasurementData> measureLocalSearchMemoryAll() throws ViatraQueryException {
		return measureMemoryAll(returnQuerySpecifications(), EngineImpl.LOCAL_SEARCH);
	}
	
	public List<MeasurementData> measureReteTimeAll() throws Exception {
		return measureTimeAll(returnQuerySpecifications(), EngineImpl.RETE);
	}
	
	public List<MeasurementData> measureLocalSearchTimeAll() throws Exception {
		return measureTimeAll(returnQuerySpecifications(), EngineImpl.LOCAL_SEARCH);
	}
	
	public List<MeasurementData> measureHybridTimeAll() throws Exception {
		return measureTimeAll(returnIncrementalQuerySpecifications(), EngineImpl.LOCAL_SEARCH);
	}
	
	public MeasurementData measureSingleQuery(IQuerySpecification<?> specification, List<IQuerySpecification<?>> hintedPatterns) throws ViatraQueryException, InvocationTargetException {
		MeasurementData measurementData = new MeasurementData(specification.getFullyQualifiedName());
		AdvancedViatraQueryEngine engine = createHintedLocalSearchEngine(hintedPatterns);
		LocalSearchProfilerAdapter profiler = createProfiler(engine); // Profiler init				
		// Setting the indexes to decrease the "doubled" traversal time of local search
		setIndex(specification, engine);
		System.out.println("Measuring time: " + specification.getFullyQualifiedName().substring(specification.getFullyQualifiedName().lastIndexOf(".") + 1) + "...");
		ViatraQueryMatcher<? extends IPatternMatch> matcher = measureMatcherInitTime(engine, specification, measurementData); // Measure matcher init time			
		measureMatchReturnTime(matcher, measurementData); // Measure match return time
		engine.dispose();
		measurementData.setPlanExecution(profiler.toString());
		return measurementData;
	}

	private void setIndex(IQuerySpecification<?> specification, AdvancedViatraQueryEngine engine)
			throws ViatraQueryException, InvocationTargetException {
		final NavigationHelper index = EMFScope.extractUnderlyingEMFIndex(engine);
		index.coalesceTraversals(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				Set<EClass> classes = new HashSet<EClass>();
				Set<EDataType> dataTypes = new HashSet<EDataType>();
				Set<EStructuralFeature> features = new HashSet<EStructuralFeature>();
				for (PQuery query : specification.getInternalQueryRepresentation().getAllReferredQueries()) {
					for (PBody body : query.getDisjunctBodies().getBodies()) {
			            for (PConstraint constraint : body.getConstraints()) {
			                if (constraint instanceof TypeConstraint) {
			                    IInputKey supplierKey = ((TypeConstraint) constraint).getSupplierKey();
			                    if (supplierKey instanceof EClassTransitiveInstancesKey) {
			                    	EClass eClass = ((EClassTransitiveInstancesKey) supplierKey).getEmfKey();
			                    	classes.add(eClass);
			                    }
			                    else if (supplierKey instanceof EClassUnscopedTransitiveInstancesKey) {
			                    	EClass eClass = ((EClassUnscopedTransitiveInstancesKey) supplierKey).getEmfKey();
			                    	classes.add(eClass);
			                    }
			                    else if (supplierKey instanceof EDataTypeInSlotsKey) {
			                    	EDataType eDataType = ((EDataTypeInSlotsKey) supplierKey).getEmfKey();
			                    	dataTypes.add(eDataType);
			                    }
			                    else if (supplierKey instanceof EStructuralFeatureInstancesKey) {
			                    	EStructuralFeature eStructuralFeature = ((EStructuralFeatureInstancesKey) supplierKey).getEmfKey();
			                    	features.add(eStructuralFeature);
			                    }
			                }
			            }
			        }
				}
				index.registerEClasses(classes, IndexingLevel.FULL);
				index.registerEDataTypes(dataTypes, IndexingLevel.FULL);
				index.registerEStructuralFeatures(features, IndexingLevel.FULL);
				return null;
			}			
		});
	}
	
	private List<MeasurementData> measureTimeAll(List<IQuerySpecification<?>> querySpecifications, EngineImpl engineImpl) throws Exception {
		List<MeasurementData> measurementDatas = new ArrayList<MeasurementData>();
		for (IQuerySpecification<?> specification : querySpecifications) {
			MeasurementData measurementData = new MeasurementData(specification.getFullyQualifiedName());  
			AdvancedViatraQueryEngine engine = createEngine(engineImpl);
			setIndex(specification, engine);
			System.out.println("Measuring time " + specification.getFullyQualifiedName().substring(specification.getFullyQualifiedName().lastIndexOf(".") + 1) + "...");
			ViatraQueryMatcher<? extends IPatternMatch> matcher = measureMatcherInitTime(engine, specification, measurementData); // Measure matcher init time
			measureMatchReturnTime(matcher, measurementData); // Measure match return time
			engine.dispose();
			measurementDatas.add(measurementData);
		}
		return measurementDatas;
	}
	
	private AdvancedViatraQueryEngine createEngine(EngineImpl engineImpl) throws ViatraQueryException {
		switch (engineImpl) {
		case RETE:
			return createReteEngine();
		case LOCAL_SEARCH:
			return createLocalSearchEngine();
		default:
			throw new IllegalArgumentException("No such engine: " + engineImpl);
		}
	}

	private AdvancedViatraQueryEngine createReteEngine() throws ViatraQueryException {
		BaseIndexOptions baseIndexOptions = new BaseIndexOptions().withFeatureFilterConfiguration(new IBaseIndexFeatureFilter() {
					@Override
					public boolean isFiltered(EStructuralFeature arg0) {
						if (arg0 instanceof EReference && ((EReference) arg0).isContainment()) {
							return arg0.getName().contains("_from_");
						}
						return false;
					}
				}).withStrictNotificationMode(false);
		AdvancedViatraQueryEngine engine = AdvancedViatraQueryEngine.createUnmanagedEngine(
				new EMFScope(Application.getInstance().getProject().getModel(), baseIndexOptions));
		return engine;
	}

	private AdvancedViatraQueryEngine createLocalSearchEngine() throws ViatraQueryException {
		BaseIndexOptions baseIndexOptions = new BaseIndexOptions().withFeatureFilterConfiguration(new IBaseIndexFeatureFilter() {
					@Override
					public boolean isFiltered(EStructuralFeature arg0) {
						if (arg0 instanceof EReference && ((EReference) arg0).isContainment()) {
							return arg0.getName().contains("_from_");
						}
						return false;
					}
				}).withStrictNotificationMode(false);
		ViatraQueryEngineOptions engineOptions = ViatraQueryEngineOptions.defineOptions()
				.withDefaultBackend(LocalSearchBackendFactory.INSTANCE).build();
		AdvancedViatraQueryEngine engine = AdvancedViatraQueryEngine.createUnmanagedEngine(
				new EMFScope(Application.getInstance().getProject().getModel(), baseIndexOptions), engineOptions);
		return engine;
	}
	
	private AdvancedViatraQueryEngine createHintedLocalSearchEngine(List<IQuerySpecification<?>> patternNames) throws ViatraQueryException {
		BaseIndexOptions baseIndexOptions = new BaseIndexOptions().withFeatureFilterConfiguration(new IBaseIndexFeatureFilter() {
					@Override
					public boolean isFiltered(EStructuralFeature arg0) {
						if (arg0 instanceof EReference && ((EReference) arg0).isContainment()) {
							return arg0.getName().contains("_from_");
						}
						return false;
					}
				}).withStrictNotificationMode(false);
		QueryEvaluationHint hints = getHints(patternNames);
		ViatraQueryEngineOptions engineOptions = ViatraQueryEngineOptions.defineOptions()
				.withDefaultBackend(LocalSearchBackendFactory.INSTANCE).withDefaultHint(hints).build();
		AdvancedViatraQueryEngine engine = AdvancedViatraQueryEngine.createUnmanagedEngine(
				new EMFScope(Application.getInstance().getProject().getModel(), baseIndexOptions), engineOptions);
		return engine;
	}

	private QueryEvaluationHint getHints(List<IQuerySpecification<?>> queries) {
		return LocalSearchHints.getDefault().setCostFunction(new IndexerBasedConstraintCostFunction() {
			@Override
			public double calculateCost(PConstraint constraint,	IConstraintEvaluationContext input) {
				try {
				if (constraint instanceof IQueryReference) {
					String patternName = ((IQueryReference) constraint).getReferredQuery().getFullyQualifiedName();
					if (queries.stream().filter(it -> it.getFullyQualifiedName().equals(patternName)).count() > 0) {
						return 1.0d; 
					}
				}
				return super.calculateCost(constraint, input);
				} catch (Exception e) { e.printStackTrace(); return 0;}
			}
		}).build();
	}	

	private List<IQuerySpecification<?>> returnQuerySpecifications() throws ViatraQueryException {
		final Set<IQuerySpecification<?>> specifications = APerformanceQueries.instance().getSpecifications();
		List<IQuerySpecification<?>> specificationList = specifications.stream().collect(Collectors.toList());
		specificationList.sort((a, b) -> a.getFullyQualifiedName().compareTo(b.getFullyQualifiedName()));
		return specificationList;
	}
	
	private List<IQuerySpecification<?>> returnIncrementalQuerySpecifications() throws ViatraQueryException {
		final Set<IQuerySpecification<?>> specifications = IncrementalQueries.instance().getSpecifications();
		List<IQuerySpecification<?>> specificationList = specifications.stream().collect(Collectors.toList());
		specificationList.sort((a, b) -> a.getFullyQualifiedName().compareTo(b.getFullyQualifiedName()));
		return specificationList;
	}
	
	private LocalSearchProfilerAdapter createProfiler(AdvancedViatraQueryEngine engine) throws ViatraQueryException {
		LocalSearchBackend queryBackend = (LocalSearchBackend) engine.getQueryBackend(LocalSearchBackendFactory.INSTANCE);
		LocalSearchProfilerAdapter profiler = new LocalSearchProfilerAdapter();
		queryBackend.addAdapter(profiler);
		return profiler;
	}
	
	private List<MeasurementData> measureMemoryAll(List<IQuerySpecification<?>> querySpecifications, EngineImpl engineImpl) throws ViatraQueryException {
		List<MeasurementData> measurementDatas = new ArrayList<MeasurementData>();
		for (IQuerySpecification<?> specification : querySpecifications) {
			System.out.println("Measuring memory: " + specification.getFullyQualifiedName().substring(specification.getFullyQualifiedName().lastIndexOf(".") + 1) + "...");
			MeasurementData measurementData = new MeasurementData(specification.getFullyQualifiedName());
			AdvancedViatraQueryEngine engine = createEngine(engineImpl);
			ViatraQueryMatcher<? extends IPatternMatch> matcher = measureMatcherMemory(engine, specification, measurementData); // Measure matcher memory consumption
			measureResultSetMemory(matcher, measurementData); // Measure result set memory consumption
			engine.dispose();
			measurementDatas.add(measurementData);
		}
		return measurementDatas;
	}

	private ViatraQueryMatcher<? extends IPatternMatch> measureMatcherInitTime(AdvancedViatraQueryEngine engine, IQuerySpecification<?> specification,
			MeasurementData measurementData) throws ViatraQueryException {
		Stopwatch stopwatch = Stopwatch.createUnstarted();
		stopwatch.start(); // Start time
		ViatraQueryMatcher<? extends IPatternMatch> matcher = specification.getMatcher(engine);
		stopwatch.stop(); // End time
		// Setting elapsed time
		final long elapsedTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		measurementData.addData(elapsedTime); 
		return matcher;
	}

	private void measureMatchReturnTime(ViatraQueryMatcher<? extends IPatternMatch> matcher, MeasurementData measurementData) throws ViatraQueryException {
		Stopwatch stopwatch = Stopwatch.createUnstarted();
		stopwatch.start(); // Start time
		Collection<? extends IPatternMatch> matches = matcher.getAllMatches();
		stopwatch.stop(); // End time
		// Setting elapsed time
		final long elapsedTime= stopwatch.elapsed(TimeUnit.MILLISECONDS);
		measurementData.addData(elapsedTime);
	}

	private ViatraQueryMatcher<? extends IPatternMatch> measureMatcherMemory(AdvancedViatraQueryEngine engine, IQuerySpecification<?> specification,
			MeasurementData measurementData) throws ViatraQueryException {
		long beforeMemory = this.measureMemory(5); // Start memory
		ViatraQueryMatcher<? extends IPatternMatch> matcher = specification.getMatcher(engine);
		long afterMemory = this.measureMemory(5); // End memory
		// Saving used memory
		measurementData.addData((afterMemory - beforeMemory) / 1024);
		return matcher;
	}

	private void measureResultSetMemory(ViatraQueryMatcher<? extends IPatternMatch> matcher, MeasurementData measurementData) throws ViatraQueryException {
		long beforeMemory = this.measureMemory(5); // Start memory
		Collection<? extends IPatternMatch> matches = matcher.getAllMatches();
		long afterMemory = this.measureMemory(5); // End memory
		// Saving used memory
		measurementData.addData((afterMemory - beforeMemory) / 1024);
	}

	/**
	 * Returns the memory used by JVM in bytes.
	 */
	private long measureMemory(int gcCount) {
		for (int i = 0; i < gcCount; ++i) {
			System.gc();
		}
		try {
			if (gcCount > 0) {
				Thread.sleep(1000); // Wait for the GC to settle
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		return usedMemory;
	}
	
	public void print(List<MeasurementData> measurementDatas, String CSVFields, String path) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(new File(path))) {
			writer.println(CSVFields);
			System.out.println(CSVFields);
			for (MeasurementData measurementData : measurementDatas) {
				writer.println(measurementData);
				System.out.println(measurementData);
			}
		}
	}
	
	public void warmUpJvm() throws ViatraQueryException {
		AdvancedViatraQueryEngine engine = createReteEngine();
		for (IQuerySpecification<?> specification : WarmUpQueries.instance().getSpecifications()) {
			specification.getMatcher(engine).getAllMatches();
		}
		engine.dispose();
	}
	
	@Override
	public boolean isSupported() {
		return true;
	}
	
	@Override
	public boolean close() {
		return true;
	}

}
