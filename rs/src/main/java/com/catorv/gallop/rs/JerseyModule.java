package com.catorv.gallop.rs;

import com.catorv.gallop.rs.netty.NettyHttpServer;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URISyntaxException;

/**
 * Created by cator on 8/22/16.
 */
public abstract class JerseyModule extends AbstractModule {

	private ResourceConfig resourceConfig;

	@Override
	protected void configure() {
		resourceConfig = createResourceConfig();
		bind(ResourceConfig.class).toInstance(resourceConfig);
	}

	protected abstract ResourceConfig createResourceConfig();

	@Provides
	private HttpServer getHttpServer(HttpServerConfiguration config,
	                                 Injector injector,
	                                 ResourceConfig resourceConfig) {
		try {
			return new NettyHttpServer(config.buildUri(), injector, resourceConfig);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

}
