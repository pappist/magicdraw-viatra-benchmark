package com.incquerylabs.magicdraw.validation.test.runner;

import org.apache.maven.plugin.surefire.StartupReportConfiguration;
import org.apache.maven.plugin.surefire.report.DefaultReporterFactory;
import org.apache.maven.surefire.common.junit4.JUnit4RunListener;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.SimpleReportEntry;
import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;

import com.incquerylabs.magicdraw.validation.test.AllTests;
import com.incquerylabs.magicdraw.validation.test.MiniTests;
import com.nomagic.magicdraw.commandline.CommandLineAction;

public class TestRunner implements CommandLineAction{

	private StartupReportConfiguration getConfiguration() {
		return StartupReportConfiguration.defaultValue();
	}

	@Override
	public byte execute(String[] args) {
	    
	    Class<?> testSuiteClass = MiniTests.class;
	    String testSuite = System.getProperty("com.incquerylabs.magicdraw.benchmark.testsuite");
	    
	    if(AllTests.class.getSimpleName().equals(testSuite)) {
	        testSuiteClass = AllTests.class;
	    }
	    
		JUnitCore core = new JUnitCore();
		org.apache.maven.surefire.report.RunListener reporter = new DefaultReporterFactory(getConfiguration()).createReporter();
        final ReportEntry report = new SimpleReportEntry( testSuiteClass.getName(), testSuiteClass.getName() );
		try {
			reporter.testSetStarting( report );
			core.addListener(new JUnit4RunListener(reporter));
			core.run(new Computer(), testSuiteClass);
		} finally {
			// This step is responsible for creating the test logs
			reporter.testSetCompleted(report);
		}
		return 0;
	}

}
