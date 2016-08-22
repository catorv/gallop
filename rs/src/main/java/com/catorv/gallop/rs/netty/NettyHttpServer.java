package com.catorv.gallop.rs.netty;

import com.catorv.gallop.rs.HttpServer;
import com.google.inject.Injector;
import io.netty.channel.Channel;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

import static org.glassfish.jersey.netty.httpserver.GuiceNettyHttpContainerProvider.createHttp2Server;
import static org.glassfish.jersey.netty.httpserver.GuiceNettyHttpContainerProvider.createServer;

/**
 * Created by cator on 8/22/16.
 */
public class NettyHttpServer implements HttpServer {

	private URI baseUri;
	private Injector injector;
	private ResourceConfig resourceConfig;
	private boolean http2;
	private boolean block;

	public NettyHttpServer(URI baseUri, Injector injector,
	                       ResourceConfig resourceConfig,
	                       boolean http2, boolean block) {
		this.baseUri = baseUri;
		this.injector = injector;
		this.resourceConfig = resourceConfig;
		this.http2 = http2;
		this.block = block;
	}

	public NettyHttpServer(URI baseUri, Injector injector,
	                       ResourceConfig resourceConfig) {
		this(baseUri, injector, resourceConfig, true, false);
	}

	@Override
	public void run() {
		final Channel server;
		if (http2) {
			server = createHttp2Server(baseUri, resourceConfig, injector, null);
		} else {
			server = createServer(baseUri, resourceConfig, injector, block);
		}
//		ServerProperties

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				server.close();
			}
		}));

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
