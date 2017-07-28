package com.incquerylabs.magicdraw.validation.test;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.incquerylabs.instaschema.mondo.sam.MultipleQueriesScenario;
import com.incquerylabs.instaschema.mondo.sam.MyDataToken;
import com.incquerylabs.instaschema.performance.EngineImpl;
import com.incquerylabs.instaschema.performance.incrementalqueries.IncrementalQueries;
import com.incquerylabs.instaschema.performance.queries.APerformanceQueries;
import com.incquerylabs.instaschema.performance.queries.util.IncomingTransitionsQuerySpecification;
import com.incquerylabs.instaschema.performance.queries.util.ParentStateQuerySpecification;
import com.incquerylabs.instaschema.performance.queries.util.TransitiveSubstatesWithCheck3QuerySpecification;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.project.ProjectDescriptor;
import com.nomagic.magicdraw.core.project.ProjectDescriptorsFactory;
import com.nomagic.magicdraw.tests.MagicDrawTestRunner;

import eu.mondo.sam.core.BenchmarkEngine;
import eu.mondo.sam.core.metrics.MemoryMetric;
import eu.mondo.sam.core.results.JsonSerializer;

@RunWith(MagicDrawTestRunner.class)
public class MondoSamTest {

	private static final String INSTALL_ROOT = Application.environment().getInstallRoot();

	private static final int RUNS = 3;


	private static final String MODEL = "TMT";
	private static final Integer[] MODELSIZES = new Integer[]{300000, 540000, 780000, 1040000, 1200000}; 
	private static final String WARMUP_MODEL = "Warmup";

	// The input models must be stored in these folders
	private static final String INPUT_PATH = INSTALL_ROOT +"performance" + File.separator + "inputs" + File.separator;
//	private static final String TMT_PROJECT_PATH = INSTALL_ROOT +"performance" + File.separator + "inputs" + File.separator + MODEL + ".mdzip";
	private static final String WARMUP_PROJECT_PATH = INSTALL_ROOT + "performance" + File.separator + "inputs" + File.separator + WARMUP_MODEL + ".mdzip";

	private static final String RESULT_PATH = "." + File.separator + "results" + File.separator;
	private static final String WARMUP_RESULT_PATH = "." + File.separator + "results" + File.separator + "warmup" + File.separator;

	@BeforeClass
	public static void firstInit() throws Exception {
		new File(RESULT_PATH).mkdirs();
		// Opening project for the first time
		System.out.println("Warming up...");
		warmUpJvm(WARMUP_PROJECT_PATH);
		System.out.println("Real measurement starting...");

	}

	private static void openProject(String projectPath) {
		File file = new File(projectPath);
		ProjectDescriptor descriptor = ProjectDescriptorsFactory.createProjectDescriptor(file.toURI());
		Application.getInstance().getProjectsManager().loadProject(descriptor, true);

	}

	@Test
	public void runPerformanceMeasurement() throws Exception {
		for (Integer size : getModelSizes()) {
			for (int runIndex = 1; runIndex <= getNumberOfRuns(); runIndex++) {
				System.out.println("Opening "+MODEL+size+" project...");
				openProject(INPUT_PATH+MODEL+size+".mdzip");
				System.out.println("Project opened.");

				localSearchIndividually(RESULT_PATH, size, runIndex);
				reteIndividually(RESULT_PATH, size, runIndex);
				hybridIndividually(RESULT_PATH, size, runIndex);
				incomingTransitionsHint(RESULT_PATH, size, runIndex);
				parentStatesHint(RESULT_PATH, size, runIndex);

				System.out.println("Closing "+MODEL+size+" project...");
				Application.getInstance().getProjectsManager().getActiveProject().setClosing(true);
				System.out.println("The "+MODEL+size+" project has been closed.");
			}
		}


	}

    protected int getNumberOfRuns() {
        return RUNS;
    }

    protected Integer[] getModelSizes() {
        return MODELSIZES;
    }

	private void localSearchIndividually(String resultPath, Integer size, int runIndex) throws Exception {
		for (IQuerySpecification<?> querySpecification : sort(getLSQuerySpecifications())) {
			String path = resultPath+getName(querySpecification)+File.separator;
			new File(path).mkdirs();

			BenchmarkEngine engine = initBenchmark(path);

			MyDataToken token = new MyDataToken();
			MultipleQueriesScenario scenario = new MultipleQueriesScenario(getName(querySpecification),
					Collections.singletonList(querySpecification), EngineImpl.LOCAL_SEARCH, Collections.emptyList(), true, runIndex);
			scenario.setSize(size);
			engine.runBenchmark(scenario, token);
		}
	}

	private void reteIndividually(String resultPath, Integer size, int runIndex) throws Exception {
		for (IQuerySpecification<?> querySpecification : sort(getReteQuerySpecifications())) {
			String path = resultPath+getName(querySpecification)+File.separator;
			new File(path).mkdirs();

			BenchmarkEngine engine = initBenchmark(path);

			MyDataToken token = new MyDataToken();
			MultipleQueriesScenario scenario = new MultipleQueriesScenario(getName(querySpecification),
					Collections.singletonList(querySpecification), EngineImpl.RETE, Collections.emptyList(), false, runIndex);
			scenario.setSize(size);
			engine.runBenchmark(scenario, token);
		}
	}

	private void hybridIndividually(String resultPath, Integer size, int runIndex) throws Exception {
		for (IQuerySpecification<?> querySpecification : sort(getHybridQuerySpecifications())) {
			String path = resultPath+getName(querySpecification)+File.separator;
			new File(path).mkdirs();

			BenchmarkEngine engine = initBenchmark(path);

			MyDataToken token = new MyDataToken();
			MultipleQueriesScenario scenario = new MultipleQueriesScenario(getName(querySpecification),
					Collections.singletonList(querySpecification), EngineImpl.LOCAL_SEARCH, Collections.emptyList(), true, runIndex);
			scenario.setSize(size);
			engine.runBenchmark(scenario, token);
		}
	}


	private void incomingTransitionsHint(String resultPath, Integer size, int runIndex) throws Exception {
		String path = resultPath+getName(TransitiveSubstatesWithCheck3QuerySpecification.instance())+File.separator;
		new File(path).mkdirs();
		BenchmarkEngine engine = initBenchmark(path);

		MyDataToken token = new MyDataToken();
		MultipleQueriesScenario scenario = new MultipleQueriesScenario(getName(TransitiveSubstatesWithCheck3QuerySpecification.instance()),
				Collections.singletonList(TransitiveSubstatesWithCheck3QuerySpecification.instance()),
				EngineImpl.LOCAL_SEARCH, Collections.singletonList(IncomingTransitionsQuerySpecification.instance()), true, runIndex,
				"LocalSearch_ConditionFirstHint");
		scenario.setSize(size);
		engine.runBenchmark(scenario, token);
	}

	private void parentStatesHint(String resultPath, Integer size, int runIndex) throws Exception {
		String path = resultPath+getName(TransitiveSubstatesWithCheck3QuerySpecification.instance())+File.separator;
		new File(path).mkdirs();
		BenchmarkEngine engine = initBenchmark(path);


		MyDataToken token = new MyDataToken();
		MultipleQueriesScenario scenario = new MultipleQueriesScenario(getName(TransitiveSubstatesWithCheck3QuerySpecification.instance()),
				Collections.singletonList(TransitiveSubstatesWithCheck3QuerySpecification.instance()),
				EngineImpl.LOCAL_SEARCH, Collections.singletonList(ParentStateQuerySpecification.instance()), true, runIndex,
				"LocalSearch_TCFirstHint");
		scenario.setSize(size);
		engine.runBenchmark(scenario, token);
	}

	private BenchmarkEngine initBenchmark(String resultPath) {
		BenchmarkEngine engine = new BenchmarkEngine();
		JsonSerializer.setResultPath(resultPath);
		MemoryMetric.setNumberOfGC(5);
		return engine;
	}

	/**
	 * Opens the MagicDraw project specified in the path and iterates through all queries (set on the MagicDraw model) that are going to be measured in the benchmark.
	 * @param warmUpProjectPath The path of the MagicDraw model
	 */
	private static void warmUpJvm(String warmUpProjectPath) throws Exception {
		// Opening project for warm up
		openProject(warmUpProjectPath);
		// Executing the exact same phases with a trivial sized model (to avoid class loading as much as possible)
		MondoSamTest mondoSamTest = new MondoSamTest();
		mondoSamTest.localSearchIndividually(WARMUP_RESULT_PATH, 5000, 1);
		mondoSamTest.reteIndividually(WARMUP_RESULT_PATH, 5000, 1);
		mondoSamTest.hybridIndividually(WARMUP_RESULT_PATH, 5000, 1);
		mondoSamTest.incomingTransitionsHint(WARMUP_RESULT_PATH, 5000, 1);
		mondoSamTest.parentStatesHint(WARMUP_RESULT_PATH, 5000, 1);
		// Closing the project
		Application.getInstance().getProjectsManager().getActiveProject().setClosing(true);
	}

	protected Collection<IQuerySpecification<?>> getReteQuerySpecifications() throws ViatraQueryException {
	    return APerformanceQueries.instance().getSpecifications();
	}
	
	protected Collection<IQuerySpecification<?>> getLSQuerySpecifications() throws ViatraQueryException {
        return APerformanceQueries.instance().getSpecifications();
    }
	
	protected Collection<IQuerySpecification<?>> getHybridQuerySpecifications() throws ViatraQueryException {
        return IncrementalQueries.instance().getSpecifications();
    }
	
	private List<IQuerySpecification<?>> sort(Collection<IQuerySpecification<?>> querySpecifications) throws ViatraQueryException {
		
		return querySpecifications.stream().sorted((a, b) -> a.getFullyQualifiedName().compareTo(b.getFullyQualifiedName()))
				.collect(Collectors.toList());
	}

	/**
	 * Returns the simple (not fully qualified) name of a query.
	 */
	private String getName(IQuerySpecification<?> querySpecification) {
		return querySpecification.getFullyQualifiedName().substring(querySpecification.getFullyQualifiedName().lastIndexOf(".") + 1);
	}

}
