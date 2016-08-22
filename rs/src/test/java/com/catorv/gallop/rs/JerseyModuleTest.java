package com.catorv.gallop.rs;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by cator on 8/22/16.
 */
@RunWith(GuiceTestRunner.class)
public class JerseyModuleTest extends AbstractModule {
	@Override
	protected void configure() {
		install(new ConfigurationModule());
		install(new LoggerModule());
		install(new JerseyModule() {
			@Override
			protected void configureJersey(ResourceConfig resourceConfig) {
				resourceConfig.packages("com.catorv.gallop.rs.resource");
			}
		});
	}

	@Inject
	private HttpServer httpServer;

	@Test
	public void test() {
		if (httpServer != null) {
//			httpServer.run();
		}
	}
}
