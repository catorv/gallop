package com.catorv.gallop.httpclient;

import com.catorv.gallop.util.TypeCast;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.apache.http.HttpHost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

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

		Registry<ConnectionSocketFactory> socketFactoryRegistry = null;
		try {
			final SSLContext sslContext = SSLContexts.custom()
					.loadTrustMaterial(null, new TrustSelfSignedStrategy())
					.build();
			final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext, NoopHostnameVerifier.INSTANCE);
			socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslsf)
					.build();
		} catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
			e.printStackTrace();
		}

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
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
				final String hostname = s.substring(0, pos);
				final HttpHost host = HttpHost.create(hostname);
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
