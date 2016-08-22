package com.catorv.gallop.rs;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by cator on 8/22/16.
 */
public abstract class HttpServerRunner implements Runnable {

	@Inject
	protected ResourceConfig resourceConfig;

	@Inject
	protected HttpServerConfiguration config;

	@Inject
	protected Injector injector;

	protected abstract void configure();

	protected abstract HttpServer createHttpServer();

	@Override
	public void run() {
		configure();
		HttpServer httpServer = createHttpServer();
		if (httpServer != null) {
			httpServer.run();
		}
	}
}
