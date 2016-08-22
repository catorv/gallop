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

		if (config.getRequestExpectContinueEnabled() != null) {
			builder.setExpectContinueEnabled(config.getRequestExpectContinueEnabled());
		}

		String proxy = config.getRequestProxy();
		if (!Strings.isNullOrEmpty(proxy)) {
			builder.setProxy(new HttpHost(proxy));
		}

		String localAddress = config.getRequestLocalAddress();
		if (!Strings.isNullOrEmpty(localAddress)) {
			try {
				InetAddress address = InetAddress.getByName(localAddress);
				builder.setLocalAddress(address);
			} catch (UnknownHostException ignored) {
			}
		}

		String cookieSpec = config.getRequestCookieSpec();
		if (!Strings.isNullOrEmpty(cookieSpec)) {
			builder.setCookieSpec(cookieSpec);
		}

		if (config.getRequestRedirectsEnabled() != null) {
			builder.setRedirectsEnabled(config.getRequestRedirectsEnabled());
		}

		if (config.getRequestRelativeRedirectsAllowed() != null) {
			builder.setRelativeRedirectsAllowed(config.getRequestRelativeRedirectsAllowed());
		}

		if (config.getRequestCircularRedirectsAllowed() != null) {
			builder.setCircularRedirectsAllowed(config.getRequestCircularRedirectsAllowed());
		}

		if (config.getRequestMaxRedirects() != null) {
			builder.setMaxRedirects(config.getRequestMaxRedirects());
		}

		if (config.getRequestAuthenticationEnabled() != null) {
			builder.setAuthenticationEnabled(config.getRequestAuthenticationEnabled());
		}

		String[] targetPreferredAuthSchemes = config.getRequestTargetPreferredAuthSchemes();
		if (targetPreferredAuthSchemes != null && targetPreferredAuthSchemes.length > 0) {
			builder.setTargetPreferredAuthSchemes(Arrays.asList(targetPreferredAuthSchemes));
		}

		String[] proxyPreferredAuthSchemes = config.getRequestProxyPreferredAuthSchemes();
		if (proxyPreferredAuthSchemes != null && proxyPreferredAuthSchemes.length > 0) {
			builder.setProxyPreferredAuthSchemes(Arrays.asList(proxyPreferredAuthSchemes));
		}

		if (config.getRequestConnectionRequestTimeout() != null) {
			builder.setConnectionRequestTimeout(config.getRequestConnectionRequestTimeout());
		}

		if (config.getRequestConnectTimeout() != null) {
			builder.setConnectTimeout(config.getRequestConnectTimeout());
		}

		if (config.getRequestSocketTimeout() != null) {
			builder.setSocketTimeout(config.getRequestSocketTimeout());
		}

		if (config.getRequestContentCompressionEnabled() != null) {
			builder.setContentCompressionEnabled(config.getRequestContentCompressionEnabled());
		}

		requestConfig = builder.build();
		return requestConfig;
	}
}
