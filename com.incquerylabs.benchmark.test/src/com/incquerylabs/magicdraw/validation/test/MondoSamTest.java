package com.incquerylabs.magicdraw.validation.test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
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
 	private static String COMMON_LAYOUT = "%c{1} - %m%n";
	private static String FILE_LOG_LAYOUT_PREFIX = "[%d{MMM/dd HH:mm:ss}] ";
	private static final String INSTALL_ROOT = Application.environment().getInstallRoot();

	private static final int RUNS = 3;


	private static final String MODEL = "TMT";
	private static final String WARMUP_MODEL = "Warmup";

	// The input models must be stored in these folders
	private static final String INPUT_PATH = INSTALL_ROOT +"performance" + File.separator + "inputs" + File.separator;
//	private static final String TMT_PROJECT_PATH = INSTALL_ROOT +"performance" + File.separator + "inputs" + File.separator + MODEL + ".mdzip";
	private static final String WARMUP_PROJECT_PATH = INSTALL_ROOT + "performance" + File.separator + "inputs" + File.separator + WARMUP_MODEL;

	private static final String RESULT_PATH = "." + File.separator + "results" + File.separator;
	private static final String WARMUP_RESULT_PATH = "." + File.separator + "results" + File.separator + "warmup" + File.separator;

	@BeforeClass
	public static void firstInit() throws Exception {
		new File(RESULT_PATH).mkdirs();
		// Opening project for the first time
		System.out.println("Warming up...");
		warmUpJvm(WARMUP_PROJECT_PATH);
		System.out.println("Real measurement starting...");
		initLogger();

	}

	private static void openProject(String projectPath) {
		System.out.println("Opening "+projectPath+" project...");
		File file = new File(projectPath);
		ProjectDescriptor descriptor = ProjectDescriptorsFactory.createProjectDescriptor(file.toURI());
		Application.getInstance().getProjectsManager().loadProject(descriptor, true);
		System.out.println("Project opened.");
	}

	@Test
	public void runPerformanceMeasurement() throws Exception {
	    int size = getModelSize();
	    int runIndex = getRunIndex();
	    String modelPath = INPUT_PATH+MODEL+size;
	    runMeasurementOnModel(modelPath, RESULT_PATH, size, runIndex);
	}
	
	protected void runMeasurementOnModel(String modelPath, String resultPath, int size, int runIndex) throws Exception {
		
        	String engine = getEngine();
		if("RETE".equals(engine)) {
			
			List<IQuerySpecification<?>> specs = sort(getReteQuerySpecifications());
	        if (specs.isEmpty()) {
	            return;
	        }
		    openProject(modelPath+".mdzip");
		    reteIndividually(resultPath, size, runIndex);
		    Application.getInstance().getProjectsManager().closeProjectNoSave();
		} else if ("LOCAL_SEARCH".equals(engine)) {
		    List<IQuerySpecification<?>> specs = sort(getLSQuerySpecifications());
	        if (specs.isEmpty()) {
	            return;
	        }
		    openProject(modelPath+".mdzip");
		    localSearchIndividually(resultPath, size, runIndex);
		    Application.getInstance().getProjectsManager().closeProjectNoSave();
		} else if ("LOCAL_SEARCH_HINTS-CONDITION_FIRST".equals(engine)) {
		    if(!getQuery().equals(getName(TransitiveSubstatesWithCheck3QuerySpecification.instance()))) {
	            return;
	        }
		    openProject(modelPath+".mdzip");
		    incomingTransitionsHint(resultPath, size, runIndex);
		    Application.getInstance().getProjectsManager().closeProjectNoSave();
		} else if ("LOCAL_SEARCH_HINTS-TC_FIRST".equals(engine)) {
		    if(!getQuery().equals(getName(TransitiveSubstatesWithCheck3QuerySpecification.instance()))) {
	            return;
	        }
		    openProject(modelPath+".mdzip");
		    parentStatesHint(resultPath, size, runIndex);
		    Application.getInstance().getProjectsManager().closeProjectNoSave();
		} else if ("HYBRID".equals(engine)) {
		    List<IQuerySpecification<?>> specs = sort(getHybridQuerySpecifications());
		    if (specs.isEmpty()) {
		        return;
		    }
		    openProject(modelPath+".mdzip");
		    hybridIndividually(resultPath, size, runIndex);
		    Application.getInstance().getProjectsManager().closeProjectNoSave();
		}

	}

    protected int getRunIndex() {
        String runIndexString = System.getProperty("com.incquerylabs.magicdraw.benchmark.runIndex");
        
        int runIndex = 1;
        if (runIndexString != null) {
            runIndex = Integer.parseInt(runIndexString);
        }
        return runIndex;
    }

    protected int getModelSize() {
        String modelSizeString = System.getProperty("com.incquerylabs.magicdraw.benchmark.size");
        
        int modelSize = 300000;
        if (modelSizeString != null) {
            modelSize = Integer.parseInt(modelSizeString);
        }
        return modelSize;
    }
    
    protected String getEngine() {
        String engine = System.getProperty("com.incquerylabs.magicdraw.benchmark.engine");
        
        if (engine == null) {
            engine = "RETE";
        }
        return engine;
    }
    
    protected String getQuery() {
        String query = System.getProperty("com.incquerylabs.magicdraw.benchmark.query");
        
        if (query == null) {
            query = "transitiveSubstatesWithCheck3";
        }
        return query;
    }

	private void localSearchIndividually(String resultPath, Integer size, int runIndex) throws Exception {
	    List<IQuerySpecification<?>> specs = sort(getLSQuerySpecifications());
	    for (IQuerySpecification<?> querySpecification : specs) {
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
	    List<IQuerySpecification<?>> specs = sort(getReteQuerySpecifications());
        for (IQuerySpecification<?> querySpecification : specs) {
			
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
	    List<IQuerySpecification<?>> specs = sort(getHybridQuerySpecifications());
        for (IQuerySpecification<?> querySpecification : specs) {
			
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
	    if(!getQuery().equals(getName(TransitiveSubstatesWithCheck3QuerySpecification.instance()))) {
            return;
        }
	    
	    
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
		// Executing the exact same phases with a trivial sized model (to avoid class loading as much as possible)
		MondoSamTest mondoSamTest = new MondoSamTest();
		mondoSamTest.runMeasurementOnModel(warmUpProjectPath, WARMUP_RESULT_PATH, 5000, 1);
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
		String query = getQuery();
		return querySpecifications.stream().sorted((a, b) -> a.getFullyQualifiedName().compareTo(b.getFullyQualifiedName()))
		        .filter(spec -> getName(spec).equals(query)).collect(Collectors.toList());
	}

	/**
	 * Returns the simple (not fully qualified) name of a query.
	 */
	private String getName(IQuerySpecification<?> querySpecification) {
		return querySpecification.getFullyQualifiedName().substring(querySpecification.getFullyQualifiedName().lastIndexOf(".") + 1);
	}
	
	private static void initLogger() throws IOException {	
		Logger logger = Logger.getLogger("org.eclipse.viatra.query");
		logger.setLevel(Level.INFO);
		
		String logFilePath = RESULT_PATH+"log/benchmark.log";
		FileAppender fileAppender = new FileAppender(new PatternLayout(FILE_LOG_LAYOUT_PREFIX+COMMON_LAYOUT),logFilePath,true);
		logger.addAppender(fileAppender);
	}

}
