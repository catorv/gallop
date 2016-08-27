package com.catorv.test.gallop.rs.resource;

import com.catorv.gallop.log.InjectLogger;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;

/**
 * Created by cator on 8/22/16.
 */
public class GuiceService {

	@InjectLogger
	private Logger logger;

	@Inject
	@Named("user.home")
	private String namedValue;

	public String getNamedValue() {
		return namedValue;
	}

	public void setNamedValue(String namedValue) {
		this.namedValue = namedValue;
	}
}
