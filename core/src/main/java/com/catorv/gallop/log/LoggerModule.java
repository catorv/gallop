package com.catorv.gallop.log;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * Logger Module
 * Created by cator on 8/1/16.
 */
public class LoggerModule extends AbstractModule {

	@Override
	protected void configure() {
		bindListener(Matchers.any(), new Slf4jLoggerInjectListener());
	}

}

