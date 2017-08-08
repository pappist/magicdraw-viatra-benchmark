package com.incquerylabs.instaschema.performance.benchmark;

import java.io.File;

import org.eclipse.viatra.query.runtime.api.IQuerySpecification;

import com.incquerylabs.instaschema.mondo.sam.QueryMatcherToken;
import com.incquerylabs.instaschema.mondo.sam.SingleQueryScenario;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.project.ProjectDescriptor;
import com.nomagic.magicdraw.core.project.ProjectDescriptorsFactory;

import eu.mondo.sam.core.BenchmarkEngine;
import eu.mondo.sam.core.metrics.MemoryMetric;
import eu.mondo.sam.core.results.JsonSerializer;

public class MondoSamRunner {
	
	private final BenchmarkParameters parameters;
	

	public MondoSamRunner(BenchmarkParameters parameters) {
		this.parameters = parameters;
	}

	private static void openProject(String projectPath) {
		System.out.println("Opening "+projectPath+" project...");
		File file = new File(projectPath);
		ProjectDescriptor descriptor = ProjectDescriptorsFactory.createProjectDescriptor(file.toURI());
		Application.getInstance().getProjectsManager().loadProject(descriptor, true);
		System.out.println("Project opened.");
	}

	@SuppressWarnings("deprecation")
	public void runPerformanceMeasurement(boolean isWarmup) throws Exception {
	    int size = parameters.getModelSize();
	    int runIndex = parameters.getRunIndex();
	    
	    String modelPath = parameters.getProjectPath(isWarmup);
		String resultPath = parameters.getResultPath(isWarmup);
	    BackendSelection queryBackend = parameters.getSelectedBackend();
	    if (!queryBackend.canHandleParameters(parameters)) {
	    		throw new IllegalArgumentException("Invalid parameter configuration");
	    }
	    openProject(modelPath);
	    runBenchmark(BackendSelection.RETE, resultPath, size, runIndex);
	    Application.getInstance().getProjectsManager().closeProjectNoSave();
	}

	private void runBenchmark(BackendSelection backend, String resultPath, Integer size, int runIndex) throws Exception {
		IQuerySpecification<?> querySpecification = backend.findQuery(parameters);
		String path = resultPath+getName(querySpecification)+File.separator;
		new File(path).mkdirs();
		
		BenchmarkEngine engine = new BenchmarkEngine();
		JsonSerializer.setResultPath(path);
		MemoryMetric.setNumberOfGC(5);
		
		QueryMatcherToken token = new QueryMatcherToken();
		SingleQueryScenario scenario = new SingleQueryScenario(getName(querySpecification),
				querySpecification, backend.getEngineImplementation(), backend.getEngineDefaultHints(), backend.isPreindexingRequired(), runIndex, backend.getToolName());
		scenario.setSize(size);
		engine.runBenchmark(scenario, token);
	}

	/**
	 * Returns the simple (not fully qualified) name of a query.
	 */
	@Deprecated
	private String getName(IQuerySpecification<?> querySpecification) {
		return querySpecification.getFullyQualifiedName().substring(querySpecification.getFullyQualifiedName().lastIndexOf(".") + 1);
	}
	


}
