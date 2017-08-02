package com.incquerylabs.instaschema.performance.benchmark;

import com.nomagic.magicdraw.commandline.CommandLineAction;

public class PerformanceBenchmarkRunner implements CommandLineAction{

	@Override
	public byte execute(String[] args) {
	    
		try {
			MondoSamRunner.firstInit();
			MondoSamRunner measurement = new MondoSamRunner();
			measurement.runPerformanceMeasurement();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

}
