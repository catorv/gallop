package com.catorv.gallop.rs;

import com.catorv.gallop.cfg.Configuration;
import com.google.common.base.Strings;
import com.google.inject.Singleton;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by cator on 8/22/16.
 */
@Configuration("http.server")
@Singleton
public class HttpServerConfiguration {

	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final int DEFAULT_PORT = 8080;
	public static final  String DEFAULT_ROOT_PATH = "/";

	private String host;
	private int port;
	private String rootPath;

	public URI buildUri() throws URISyntaxException {
		String host = getHost();
		int port = getPort();
		String rootPath = getRootPath();
		return new URIBuilder().setScheme(port == 443 ? "https" : "http")
				.setHost(host).setPort(port).setPath(rootPath).build();
	}

	public String getHost() {
		if (Strings.isNullOrEmpty(host)) {
			host = System.getProperty("http.server.host");
			if (Strings.isNullOrEmpty(host)) {
				host = DEFAULT_HOST;
			}
		}
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		if (port > 0) {
			return port;
		}
		String portValue = System.getProperty("http.server.port");
		try {
			if (!Strings.isNullOrEmpty(portValue)) {
				port = Integer.parseInt(portValue);
				return port;
			}
		} catch (NumberFormatException ignored) {

		}
		return DEFAULT_PORT;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRootPath() {
		if (Strings.isNullOrEmpty(rootPath)) {
			rootPath = System.getProperty("http.server.rootPath");
			if (Strings.isNullOrEmpty(rootPath)) {
				rootPath = DEFAULT_ROOT_PATH;
			}
		}
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
}
