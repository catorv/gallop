package com.catorv.gallop.job.schedule;

import com.google.inject.name.Named;

/**
 * Schedule Configuration
 * Created by cator on 8/10/16.
 */
public class ScheduleConfiguration {

	@Named("quartz.configFile")
	private String quartzConfigFile;

	public String getQuartzConfigFile() {
		return quartzConfigFile;
	}

	public void setQuartzConfigFile(String quartzConfigFile) {
		this.quartzConfigFile = quartzConfigFile;
	}
}
