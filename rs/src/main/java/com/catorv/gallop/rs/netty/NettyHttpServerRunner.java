package com.catorv.gallop.rs.netty;

import com.catorv.gallop.rs.HttpServer;
import com.catorv.gallop.rs.HttpServerRunner;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by cator on 8/22/16.
 */
public abstract class NettyHttpServerRunner extends HttpServerRunner {

	protected boolean http2Enabled = true;

	protected boolean block = false;

	protected abstract void configure();

	@Override
	protected HttpServer createHttpServer() {
		try {
			final URI baseUri = config.buildUri();
			return new NettyHttpServer(baseUri, injector, resourceConfig,
					http2Enabled, block);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

}
