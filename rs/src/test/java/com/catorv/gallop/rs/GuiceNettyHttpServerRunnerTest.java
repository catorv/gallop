package com.catorv.gallop.rs;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Module Test
 * Created by cator on 8/19/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		ConfigurationModule.class,
		LoggerModule.class
})
public class GuiceNettyHttpServerRunnerTest {

	@Inject
	GuiceNettyHttpServerRunner httpServerRunner;

	@Test
	public void test() {
		Assert.assertNotNull(httpServerRunner);
//		httpServerRunner.run();
	}

	@Test
	public void test2() throws URISyntaxException {

		Injector injector = Guice.createInjector(
				new ConfigurationModule(),
				new LoggerModule()
		);

		HttpServerConfiguration config = injector.getInstance(HttpServerConfiguration.class);
		URI baseUri = config.buildUri();

		ResourceConfig resourceConfig = injector.getInstance(ResourceConfig.class);
		resourceConfig.packages("com.catorv.gallop.rs.resource");

//		new NettyHttpServer(baseUri, injector, resourceConfig).run();
	}


}
