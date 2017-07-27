package com.incquerylabs.magicdraw.validation.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.incquerylabs.instaschema.performance.InstaSchemaPerformancePlugin;
import com.incquerylabs.instaschema.performance.MeasurementData;
import com.incquerylabs.instaschema.performance.queries.util.IncomingTransitionsQuerySpecification;
import com.incquerylabs.instaschema.performance.queries.util.ParentStateQuerySpecification;
import com.incquerylabs.instaschema.performance.queries.util.StatesWithShortNamesQuerySpecification;
import com.incquerylabs.instaschema.performance.queries.util.TransitiveSubstatesWithCheck3QuerySpecification;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.project.ProjectDescriptor;
import com.nomagic.magicdraw.core.project.ProjectDescriptorsFactory;
import com.nomagic.magicdraw.tests.MagicDrawTestRunner;

@SuppressWarnings("deprecation")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MagicDrawTestRunner.class)
public class SmokeTest {
	
	private static final String TMT_PROJECT_PATH = "F:\\magicdraw_projects\\TMT_1M.mdzip";
	private static final String RESULT_PATH = "F:\\bme\\8.felev\\szakmai_gyak\\meresi_eredmenyek\\07_11_1M_1.7_Indexed#4\\";
	
	private static InstaSchemaPerformancePlugin plugin;	
	
	@BeforeClass
	public static void firstInit() {
		// Creating the destination folder
		new File(RESULT_PATH).mkdir();
		// The project has to be opened and closed before the test cases, otherwise the first test case fails
		// Opening project for the first time
		openProject(TMT_PROJECT_PATH);
		// Closing the project
		Application.getInstance().getProjectsManager().getActiveProject().setClosing(true);
		System.out.println("Initialization has been finished.");
	}

	private static void openProject(String projectPath) {
		File file = new File(projectPath);
		ProjectDescriptor descriptor = ProjectDescriptorsFactory.createProjectDescriptor(file.toURI());
		Application.getInstance().getProjectsManager().loadProject(descriptor, true);
	}
	
	@Before
	public void initialize() throws ViatraQueryException {
		System.out.println("Loading the TMT project...");
		openProject(TMT_PROJECT_PATH);
		plugin = new InstaSchemaPerformancePlugin();
		plugin.warmUpJvm();
		System.out.println("The JVM has been warmed up.");
	}
	
	@After
	public void dispose() throws ViatraQueryException {
		System.out.println("Closing the TMT project...");
		Application.getInstance().getProjectsManager().getActiveProject().setClosing(true); // throws exceptions but the code works
		System.out.println("The TMT project has been closed.");
		plugin = null;
	}
	
	@Test
	public void initializedPluginTest() throws FileNotFoundException, ViatraQueryException {		
		assertTrue(InstaSchemaPerformancePlugin.initialized);
	}
	
	@Ignore
	@Test
	public void measureReteMemory() throws FileNotFoundException, ViatraQueryException {		
		List<MeasurementData> measurementDatas = plugin.measureReteMemoryAll();
		plugin.print(measurementDatas, MeasurementData.getMemoryCSVFields(), RESULT_PATH + "rete_memory_result.txt");
	}
	
	@Ignore
	@Test
	public void measureLocalSearchMemory() throws FileNotFoundException, ViatraQueryException {		
		List<MeasurementData> measurementDatas = plugin.measureLocalSearchMemoryAll();
		plugin.print(measurementDatas, MeasurementData.getMemoryCSVFields(), RESULT_PATH + "local_search_memory_result.txt");
	}	
	
	@Ignore
	@Test
	public void measureRete() throws Exception {		
		List<MeasurementData> measurementDatas = plugin.measureReteTimeAll();
		plugin.print(measurementDatas, MeasurementData.getTimeCSVFields(), RESULT_PATH + "rete_result.txt");
	}

	@Test
	public void measureLocalSearch() throws Exception {		
		List<MeasurementData> measurementDatas = plugin.measureLocalSearchTimeAll();
		plugin.print(measurementDatas, MeasurementData.getTimeCSVFields(), RESULT_PATH + "localsearch_result.txt");
	}
	
	@Ignore
	@Test
	public void measureHybrid() throws Exception {		
		List<MeasurementData> measurementDatas = plugin.measureHybridTimeAll();
		plugin.print(measurementDatas, MeasurementData.getTimeCSVFields(), RESULT_PATH + "hybrid_result.txt");
	}

	@Ignore
	@Test
	public void measureTransitiveSubstatesWithCheck3() throws Exception {		
		MeasurementData measurementData = plugin.measureSingleQuery(TransitiveSubstatesWithCheck3QuerySpecification.instance(), Collections.emptyList());
		plugin.print(Collections.singletonList(measurementData), MeasurementData.getTimeCSVFields(), RESULT_PATH + "TransitiveSubstatesWithCheck3_result.txt");
	}
	
	@Ignore
	@Test
	public void measureTransitiveSubstatesWithCheck3_ParentStatesHint() throws Exception {		
		MeasurementData measurementData = plugin.measureSingleQuery(TransitiveSubstatesWithCheck3QuerySpecification.instance(), 
				Arrays.asList(ParentStateQuerySpecification.instance()));
		plugin.print(Collections.singletonList(measurementData), MeasurementData.getTimeCSVFields(), RESULT_PATH + "TransitiveSubstatesWithCheck3_ParentStates_hint_result.txt");
	}
	
	@Ignore
	@Test
	public void measureTransitiveSubstatesWithCheck3_IncomingTransitionsHint() throws Exception {		
		MeasurementData measurementData = plugin.measureSingleQuery(TransitiveSubstatesWithCheck3QuerySpecification.instance(), 
				Arrays.asList(IncomingTransitionsQuerySpecification.instance()));
		plugin.print(Collections.singletonList(measurementData), MeasurementData.getTimeCSVFields(), RESULT_PATH + "TransitiveSubstatesWithCheck3_IncomingTransitions_hint_result.txt");
	}

	/**
	 * Works the same way as a simple query without hints. (As the profiler and the benchmark results show.)
	 */
	@Ignore
	@Test
	public void measureTransitiveSubstatesWithCheck3_ShortNamesHint() throws Exception {		
		MeasurementData measurementData = plugin.measureSingleQuery(TransitiveSubstatesWithCheck3QuerySpecification.instance(), 
				Arrays.asList(StatesWithShortNamesQuerySpecification.instance()));
		plugin.print(Collections.singletonList(measurementData), MeasurementData.getTimeCSVFields(), RESULT_PATH + "TransitiveSubstatesWithCheck3_ShortNames_hint_result.txt");
	}

	@Ignore
	@Test
	public void measureTransitiveSubstatesWithCheck3_2Hints() throws Exception {		
		MeasurementData measurementData = plugin.measureSingleQuery(TransitiveSubstatesWithCheck3QuerySpecification.instance(), 
				Arrays.asList(StatesWithShortNamesQuerySpecification.instance(), IncomingTransitionsQuerySpecification.instance()));
		plugin.print(Collections.singletonList(measurementData), MeasurementData.getTimeCSVFields(), RESULT_PATH + "TransitiveSubstatesWithCheck3_two_hints_result.txt");
	}
	
}
