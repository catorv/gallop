package com.catorv.gallop.httpclient;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * RequestConfig Provider
 * Created by cator on 8/14/16.
 */
@Singleton
public class RequestConfigProvider implements Provider<RequestConfig> {

	@Inject
	private HttpClientConfiguration config;

	private RequestConfig requestConfig;

	@Override
	public RequestConfig get() {
		if (requestConfig != null) {
			return requestConfig;
		}

		RequestConfig.Builder builder = RequestConfig.custom();

		final Boolean expectContinueEnabled = config.getRequestExpectContinueEnabled();
		if (expectContinueEnabled != null) {
			builder.setExpectContinueEnabled(expectContinueEnabled);
		}

		final String proxy = config.getRequestProxy();
		if (!Strings.isNullOrEmpty(proxy)) {
			builder.setProxy(new HttpHost(proxy));
		}

		final String localAddress = config.getRequestLocalAddress();
		if (!Strings.isNullOrEmpty(localAddress)) {
			try {
				final InetAddress address = InetAddress.getByName(localAddress);
				builder.setLocalAddress(address);
			} catch (UnknownHostException ignored) {
			}
		}

		final String cookieSpec = config.getRequestCookieSpec();
		if (!Strings.isNullOrEmpty(cookieSpec)) {
			builder.setCookieSpec(cookieSpec);
		}

		final Boolean redirectsEnabled = config.getRequestRedirectsEnabled();
		if (redirectsEnabled != null) {
			builder.setRedirectsEnabled(redirectsEnabled);
		}

		final Boolean relativeRedirectsAllowed = config.getRequestRelativeRedirectsAllowed();
		if (relativeRedirectsAllowed != null) {
			builder.setRelativeRedirectsAllowed(relativeRedirectsAllowed);
		}

		final Boolean circularRedirectsAllowed = config.getRequestCircularRedirectsAllowed();
		if (circularRedirectsAllowed != null) {
			builder.setCircularRedirectsAllowed(circularRedirectsAllowed);
		}

		final Integer maxRedirects = config.getRequestMaxRedirects();
		if (maxRedirects != null) {
			builder.setMaxRedirects(maxRedirects);
		}

		final Boolean authenticationEnabled = config.getRequestAuthenticationEnabled();
		if (authenticationEnabled != null) {
			builder.setAuthenticationEnabled(authenticationEnabled);
		}

		final String[] targetPreferredAuthSchemes = config.getRequestTargetPreferredAuthSchemes();
		if (targetPreferredAuthSchemes != null && targetPreferredAuthSchemes.length > 0) {
			builder.setTargetPreferredAuthSchemes(Arrays.asList(targetPreferredAuthSchemes));
		}

		final String[] proxyPreferredAuthSchemes = config.getRequestProxyPreferredAuthSchemes();
		if (proxyPreferredAuthSchemes != null && proxyPreferredAuthSchemes.length > 0) {
			builder.setProxyPreferredAuthSchemes(Arrays.asList(proxyPreferredAuthSchemes));
		}

		final Integer connectionRequestTimeout = config.getRequestConnectionRequestTimeout();
		if (connectionRequestTimeout != null) {
			builder.setConnectionRequestTimeout(connectionRequestTimeout);
		}

		final Integer connectTimeout = config.getRequestConnectTimeout();
		if (connectTimeout != null) {
			builder.setConnectTimeout(connectTimeout);
		}

		final Integer socketTimeout = config.getRequestSocketTimeout();
		if (socketTimeout != null) {
			builder.setSocketTimeout(socketTimeout);
		}

		final Boolean compressionEnabled = config.getRequestContentCompressionEnabled();
		if (compressionEnabled != null) {
			builder.setContentCompressionEnabled(compressionEnabled);
		}

		requestConfig = builder.build();
		return this.requestConfig;
	}
}
