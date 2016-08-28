package com.catorv.test.gallop.cfg;

import com.catorv.gallop.cfg.Configuration;
import com.google.inject.name.Named;

import java.util.Map;

/**
 * Created by cator on 8/28/16.
 */
@Configuration("test.config")
public class Model3 {

	@Named("grouped")
	private Map<String, Model2> groups;

	public Map<String, Model2> getGroups() {
		return groups;
	}

	public void setGroups(Map<String, Model2> groups) {
		this.groups = groups;
	}
}
