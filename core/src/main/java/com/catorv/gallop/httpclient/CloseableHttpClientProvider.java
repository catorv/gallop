package com.catorv.gallop.httpclient;

import com.catorv.gallop.lifecycle.Destroy;
import com.catorv.gallop.util.TypeCast;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * CloseableHttpClient Provider
 * Created by cator on 8/14/16.
 */
@Singleton
public class CloseableHttpClientProvider implements Provider<CloseableHttpClient> {

	@Inject
	private HttpClientConfiguration config;

	@Inject
	private HttpClientConnectionManager cm;

	@Inject
	private RequestConfig requestConfig;

	private CloseableHttpClient httpClient;

	@Override
	public CloseableHttpClient get() {
		if (httpClient != null) {
			return httpClient;
		}

		HttpClientBuilder builder = HttpClients.custom().setConnectionManager(cm);

		final Long defaultKeepAliveDuration = config.getClientDefaultKeepAliveDuration();
		final Map<String, Long> keepAliveDurationMap = new HashMap<>();
		String[] keepAliveDuration = config.getClientKeepAliveDuration();
		if (keepAliveDuration != null) {
			for (String s : keepAliveDuration) {
				int pos = s.lastIndexOf('#');
				if (pos < 0) {
					continue;
				}
				Long duration = TypeCast.longOf(s.substring(pos + 1));
				if (duration <= 0) {
					continue;
				}
				final String hostname = s.substring(0, pos);
				keepAliveDurationMap.put(hostname.toLowerCase(), duration);
			}
		}
		if (defaultKeepAliveDuration != null) {
			ConnectionKeepAliveStrategy ckaStrategy = new ConnectionKeepAliveStrategy() {

				public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
					// Honor 'keep-alive' header
					HeaderElementIterator it = new BasicHeaderElementIterator(
							response.headerIterator(HTTP.CONN_KEEP_ALIVE));
					while (it.hasNext()) {
						HeaderElement he = it.nextElement();
						final String param = he.getName();
						final String value = he.getValue();
						if (value != null && param.equalsIgnoreCase("timeout")) {
							try {
								return Long.parseLong(value) * 1000;
							} catch (NumberFormatException ignore) {
							}
						}
					}
					final HttpHost target = (HttpHost) context.getAttribute(
							HttpClientContext.HTTP_TARGET_HOST);
					final String hostName = target.getHostName().toLowerCase();
					final Long duration = keepAliveDurationMap.get(hostName);
					return duration != null ? duration : defaultKeepAliveDuration;
				}

			};
			builder.setKeepAliveStrategy(ckaStrategy);
		}

		final String proxy = config.getClientProxy();
		if (!Strings.isNullOrEmpty(proxy)) {
			builder.setProxy(new HttpHost(proxy));
		}

		final String cookieStoreClass = config.getClientCookieStoreClass();
		if (!Strings.isNullOrEmpty(cookieStoreClass)) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends CookieStore> cookieStoreType =
						(Class<? extends CookieStore>) Class.forName(cookieStoreClass);
				CookieStore cookieStore = cookieStoreType.newInstance();
				builder.setDefaultCookieStore(cookieStore);
			} catch (Exception ignored) {
			}
		}

		final String credentialsProviderClass = config.getClientCredentialsProviderClass();
		if (!Strings.isNullOrEmpty(credentialsProviderClass)) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends CredentialsProvider> credentialsProviderType =
						(Class<? extends CredentialsProvider>) Class.forName(credentialsProviderClass);
				CredentialsProvider credentialsProvider = credentialsProviderType.newInstance();
				builder.setDefaultCredentialsProvider(credentialsProvider);
			} catch (Exception ignored) {
			}
		}

		final Integer maxRetryCount = config.getClientMaxRetryCount();
		if (maxRetryCount != null) {
			HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {

				public boolean retryRequest(IOException exception, int executionCount,
				                            HttpContext context) {
					if (executionCount >= maxRetryCount) {
						// Do not retry if over max retry count
						return false;
					}
					if (exception instanceof InterruptedIOException) {
						// Timeout or connection refused
						return false;
					}
					if (exception instanceof UnknownHostException) {
						// Unknown host
						return false;
					}
					if (exception instanceof SSLException) {
						// SSL handshake exception
						return false;
					}
					HttpClientContext clientContext = HttpClientContext.adapt(context);
					HttpRequest request = clientContext.getRequest();
					// Retry if the request is considered idempotent
					return !(request instanceof HttpEntityEnclosingRequest);
				}

			};
			builder.setRetryHandler(retryHandler);
		}

		httpClient = builder.setDefaultRequestConfig(requestConfig).build();
		return httpClient;
	}

	@Destroy
	public void destroy() {
		if (httpClient != null) {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
