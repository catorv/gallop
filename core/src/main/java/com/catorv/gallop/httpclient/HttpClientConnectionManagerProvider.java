package com.catorv.gallop.httpclient;

import com.catorv.gallop.util.TypeCast;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.apache.http.HttpHost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * HttpClientConnectionManager Provider
 * Created by cator on 8/14/16.
 */
@Singleton
public class HttpClientConnectionManagerProvider implements Provider<HttpClientConnectionManager> {

	@Inject
	private HttpClientConfiguration config;

	private HttpClientConnectionManager connectionManager;

	@Override
	public HttpClientConnectionManager get() {
		if (connectionManager != null) {
			return connectionManager;
		}

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// Increase max total connection to 200
		cm.setMaxTotal(config.getConnectionMaxTotal());
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(config.getConnectionDefaultMaxPerRoute());

		String[] maxPerRoute = config.getConnectionMaxPerRoute();
		if (maxPerRoute != null) {
			for (String s : maxPerRoute) {
				int pos = s.lastIndexOf('#');
				if (pos < 0) {
					continue;
				}
				int max = TypeCast.intOf(s.substring(pos + 1));
				if (max <= 0) {
					continue;
				}
				String hostname = s.substring(0, pos);
				HttpHost host = HttpHost.create(hostname);
				cm.setMaxPerRoute(new HttpRoute(host), max);
			}
		}

		if (config.getValidateAfterInactivity() != null) {
			cm.setValidateAfterInactivity(config.getValidateAfterInactivity());
		}

		connectionManager = cm;
		return cm;
	}

}
