package com.incquerylabs.magicdraw.benchmark;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.nomagic.magicdraw.commandline.CommandLineAction;
import com.nomagic.magicdraw.core.Application;

public class PerformanceBenchmarkRunner implements CommandLineAction{

 	private static String COMMON_LAYOUT = "%c{1} - %m%n";
	private static String FILE_LOG_LAYOUT_PREFIX = "[%d{MMM/dd HH:mm:ss}] ";
	
	@Override
	public byte execute(String[] args) {
	    
		try {
			BenchmarkParameters parameters = parseParameters(args);
			
			// Ensure result path exists
			new File(parameters.getResultPath()).mkdirs();
			
			// Opening project for the first time
			System.out.println("Warming up...");
			// Executing the exact same phases with a trivial sized model (to avoid class loading as much as possible)
			MondoSamRunner warmupRound = new MondoSamRunner(parameters);
			warmupRound.runPerformanceMeasurement(true);
			
			System.out.println("Real measurement starting...");
			
			initLogger(parameters);
			
			MondoSamRunner measurement = new MondoSamRunner(parameters);
			measurement.runPerformanceMeasurement(false);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	private BenchmarkParameters parseParameters(String[] args) {
		BenchmarkParameters parameters = new BenchmarkParameters();
		
		// XXX Parameters are stored in a single args value separated by whitespace characters
		String[] arguments = args[0].trim().split("\\s");

		int argIndex = 0;
		while(argIndex < arguments.length) {
			if (Objects.equals("-engine", arguments[argIndex])) {
				parameters.setSelectedBackend(arguments[argIndex + 1]);
				argIndex+=2;
			} else if (Objects.equals("-query", arguments[argIndex])) {
				parameters.setQueryName(arguments[argIndex + 1]);
				argIndex+=2;
			} else if (Objects.equals("-index", arguments[argIndex])) {
				parameters.setRunIndex(arguments[argIndex + 1]);
				argIndex+=2;
			} else if (Objects.equals("-size", arguments[argIndex])) {
				parameters.setModelSize(arguments[argIndex + 1]);
				argIndex+=2;
			} else if (Objects.equals("-model", arguments[argIndex])) {
				parameters.setProjectPath(arguments[argIndex + 1].replaceAll("'", "").replaceAll("\"", ""));
				argIndex+=2;
			} else if (Objects.equals("-warmup", arguments[argIndex])) {
				parameters.setWarmupProjectPath(arguments[argIndex + 1].replaceAll("'", "").replaceAll("\"", ""));
				argIndex+=2;
			} else if (Objects.equals("-output", arguments[argIndex])) {
				parameters.setResultPath(arguments[argIndex + 1].replaceAll("'", "").replaceAll("\"", ""));
				argIndex+=2;
			} else {
				System.err.println("Unexpected parameter " + arguments[argIndex]);
				argIndex++; // Skip unknown parameter
			}			
		}
		
		return parameters;
	}
	
	private void initLogger(BenchmarkParameters parameters) throws IOException {	
		Logger logger = Logger.getLogger("org.eclipse.viatra.query");
		logger.setLevel(Level.INFO);
		
		String logFilePath = parameters.getResultPath()+"log/benchmark.log";
		FileAppender fileAppender = new FileAppender(new PatternLayout(FILE_LOG_LAYOUT_PREFIX+COMMON_LAYOUT),logFilePath,true);
		logger.addAppender(fileAppender);
	}
}
