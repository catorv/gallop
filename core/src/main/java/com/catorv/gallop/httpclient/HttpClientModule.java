package com.catorv.gallop.httpclient;

import com.google.inject.AbstractModule;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * HttpClient Module
 * Created by cator on 8/14/16.
 */
public class HttpClientModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(HttpClientConnectionManager.class)
				.toProvider(HttpClientConnectionManagerProvider.class);
		bind(RequestConfig.class)
				.toProvider(RequestConfigProvider.class);
		bind(CloseableHttpClient.class)
				.toProvider(CloseableHttpClientProvider.class);
	}
}
