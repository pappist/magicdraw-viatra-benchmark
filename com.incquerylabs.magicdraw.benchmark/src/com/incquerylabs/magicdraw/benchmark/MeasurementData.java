package com.incquerylabs.magicdraw.benchmark;

import java.util.ArrayList;
import java.util.List;

public class MeasurementData {

	private String queryName;
	
	private List<Long> measuredData;

	// Needed for local search only
	private String planExecution;
	// CSV separator
	private static char separator = ',';
	
	public MeasurementData(String queryName) {
		this.queryName = queryName;
		this.measuredData = new ArrayList<Long>();
	}

	public static String getTimeCSVFields() {
		return "Query name" + separator + "Matcher initialization time" + separator + "Result set return time";
	}
	
	public static String getMemoryCSVFields() {
		return "Query name" + separator + "Matcher memory size" + separator + "Result set memory size";
	}
	
	@Override
	public String toString() {
		StringBuilder result =  new StringBuilder(queryName.substring(queryName.lastIndexOf(".") + 1));
		for (Long data : measuredData) {
			result.append(separator); result.append(data);
		}
		if (planExecution != null) {
			result.append(System.lineSeparator() + planExecution);
		}
		return result.toString();
	}
	
	public String getQueryName() {
		return queryName;
	}

	public String getPlanExecution() {
		return planExecution;
	}

	public void addData(long data) {
		this.measuredData.add(data);
	}

	public List<Long> setMeasuredDatas() {
		return this.measuredData;
	}

	public void setPlanExecution(String planExecution) {
		this.planExecution = planExecution;
	}
	
	public static void setSeparator(char separator) {
		MeasurementData.separator = separator;
	}
	
}
