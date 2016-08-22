package com.catorv.gallop.httpclient;

import com.catorv.gallop.cfg.Configuration;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * HttpClient Configuration
 * Created by cator on 8/14/16.
 */
@SuppressWarnings("BindingAnnotationWithoutInject")
@Singleton
@Configuration("httpclient")
public class HttpClientConfiguration {

	// Connection Manager Configuration

	@Named("connection.maxTotal")
	private Integer connectionMaxTotal = 200;
	@Named("connection.defaultMaxPerRoute")
	private Integer connectionDefaultMaxPerRoute = 20;
	/** format: host:port#maxTotal */
	@Named("connection.maxPerRoute")
	private String[] connectionMaxPerRoute;
	@Named("connection.validateAfterInactivity")
	private Integer validateAfterInactivity;

	// Request Configuration

	@Named("request.expectContinueEnabled")
	private Boolean requestExpectContinueEnabled;
	/** format: host:post */
	@Named("request.proxy")
	private String requestProxy;
	@Named("request.localAddress")
	private String requestLocalAddress;
	@Named("request.cookieSpec")
	private String requestCookieSpec;
	@Named("request.redirectsEnabled")
	private Boolean requestRedirectsEnabled = true;
	@Named("request.relativeRedirectsAllowed")
	private Boolean requestRelativeRedirectsAllowed = true;
	@Named("request.circularRedirectsAllowed")
	private Boolean requestCircularRedirectsAllowed;
	@Named("request.maxRedirects")
	private Integer requestMaxRedirects = 50;
	@Named("request.authenticationEnabled")
	private Boolean requestAuthenticationEnabled = true;
	@Named("request.targetPreferredAuthSchemes")
	private String[] requestTargetPreferredAuthSchemes;
	@Named("request.proxyPreferredAuthSchemes")
	private String[] requestProxyPreferredAuthSchemes;
	@Named("request.connectionRequestTimeout")
	private Integer requestConnectionRequestTimeout = -1;
	@Named("request.connectTimeout")
	private Integer requestConnectTimeout = -1;
	@Named("request.socketTimeout")
	private Integer requestSocketTimeout = -1;
	@Named("request.contentCompressionEnabled")
	private Boolean requestContentCompressionEnabled = true;

	// Client Configuration

	@Named("client.defaultKeepAliveDuration")
	private Long clientDefaultKeepAliveDuration;
	/** format: hostname#duration */
	@Named("client.keepAliveDuration")
	private String[] clientKeepAliveDuration;
	@Named("client.proxy")
	private String clientProxy;
	@Named("client.cookieStoreClass")
	private String clientCookieStoreClass;
	@Named("client.credentialsProviderClass")
	private String clientCredentialsProviderClass;
	@Named("client.maxRetryCount")
	private Integer clientMaxRetryCount;

	public Integer getConnectionMaxTotal() {
		return connectionMaxTotal;
	}

	public void setConnectionMaxTotal(Integer connectionMaxTotal) {
		this.connectionMaxTotal = connectionMaxTotal;
	}

	public Integer getConnectionDefaultMaxPerRoute() {
		return connectionDefaultMaxPerRoute;
	}

	public void setConnectionDefaultMaxPerRoute(Integer connectionDefaultMaxPerRoute) {
		this.connectionDefaultMaxPerRoute = connectionDefaultMaxPerRoute;
	}

	public String[] getConnectionMaxPerRoute() {
		return connectionMaxPerRoute;
	}

	public void setConnectionMaxPerRoute(String[] connectionMaxPerRoute) {
		this.connectionMaxPerRoute = connectionMaxPerRoute;
	}

	public Integer getValidateAfterInactivity() {
		return validateAfterInactivity;
	}

	public void setValidateAfterInactivity(Integer validateAfterInactivity) {
		this.validateAfterInactivity = validateAfterInactivity;
	}

	public Boolean getRequestExpectContinueEnabled() {
		return requestExpectContinueEnabled;
	}

	public void setRequestExpectContinueEnabled(Boolean requestExpectContinueEnabled) {
		this.requestExpectContinueEnabled = requestExpectContinueEnabled;
	}

	public String getRequestProxy() {
		return requestProxy;
	}

	public void setRequestProxy(String requestProxy) {
		this.requestProxy = requestProxy;
	}

	public String getRequestLocalAddress() {
		return requestLocalAddress;
	}

	public void setRequestLocalAddress(String requestLocalAddress) {
		this.requestLocalAddress = requestLocalAddress;
	}

	public String getRequestCookieSpec() {
		return requestCookieSpec;
	}

	public void setRequestCookieSpec(String requestCookieSpec) {
		this.requestCookieSpec = requestCookieSpec;
	}

	public Boolean getRequestRedirectsEnabled() {
		return requestRedirectsEnabled;
	}

	public void setRequestRedirectsEnabled(Boolean requestRedirectsEnabled) {
		this.requestRedirectsEnabled = requestRedirectsEnabled;
	}

	public Boolean getRequestRelativeRedirectsAllowed() {
		return requestRelativeRedirectsAllowed;
	}

	public void setRequestRelativeRedirectsAllowed(Boolean requestRelativeRedirectsAllowed) {
		this.requestRelativeRedirectsAllowed = requestRelativeRedirectsAllowed;
	}

	public Boolean getRequestCircularRedirectsAllowed() {
		return requestCircularRedirectsAllowed;
	}

	public void setRequestCircularRedirectsAllowed(Boolean requestCircularRedirectsAllowed) {
		this.requestCircularRedirectsAllowed = requestCircularRedirectsAllowed;
	}

	public Integer getRequestMaxRedirects() {
		return requestMaxRedirects;
	}

	public void setRequestMaxRedirects(Integer requestMaxRedirects) {
		this.requestMaxRedirects = requestMaxRedirects;
	}

	public Boolean getRequestAuthenticationEnabled() {
		return requestAuthenticationEnabled;
	}

	public void setRequestAuthenticationEnabled(Boolean requestAuthenticationEnabled) {
		this.requestAuthenticationEnabled = requestAuthenticationEnabled;
	}

	public String[] getRequestTargetPreferredAuthSchemes() {
		return requestTargetPreferredAuthSchemes;
	}

	public void setRequestTargetPreferredAuthSchemes(String[] requestTargetPreferredAuthSchemes) {
		this.requestTargetPreferredAuthSchemes = requestTargetPreferredAuthSchemes;
	}

	public String[] getRequestProxyPreferredAuthSchemes() {
		return requestProxyPreferredAuthSchemes;
	}

	public void setRequestProxyPreferredAuthSchemes(String[] requestProxyPreferredAuthSchemes) {
		this.requestProxyPreferredAuthSchemes = requestProxyPreferredAuthSchemes;
	}

	public Integer getRequestConnectionRequestTimeout() {
		return requestConnectionRequestTimeout;
	}

	public void setRequestConnectionRequestTimeout(Integer requestConnectionRequestTimeout) {
		this.requestConnectionRequestTimeout = requestConnectionRequestTimeout;
	}

	public Integer getRequestConnectTimeout() {
		return requestConnectTimeout;
	}

	public void setRequestConnectTimeout(Integer requestConnectTimeout) {
		this.requestConnectTimeout = requestConnectTimeout;
	}

	public Integer getRequestSocketTimeout() {
		return requestSocketTimeout;
	}

	public void setRequestSocketTimeout(Integer requestSocketTimeout) {
		this.requestSocketTimeout = requestSocketTimeout;
	}

	public Boolean getRequestContentCompressionEnabled() {
		return requestContentCompressionEnabled;
	}

	public void setRequestContentCompressionEnabled(Boolean requestContentCompressionEnabled) {
		this.requestContentCompressionEnabled = requestContentCompressionEnabled;
	}

	public Long getClientDefaultKeepAliveDuration() {
		return clientDefaultKeepAliveDuration;
	}

	public void setClientDefaultKeepAliveDuration(Long clientDefaultKeepAliveDuration) {
		this.clientDefaultKeepAliveDuration = clientDefaultKeepAliveDuration;
	}

	public String[] getClientKeepAliveDuration() {
		return clientKeepAliveDuration;
	}

	public void setClientKeepAliveDuration(String[] clientKeepAliveDuration) {
		this.clientKeepAliveDuration = clientKeepAliveDuration;
	}

	public String getClientProxy() {
		return clientProxy;
	}

	public void setClientProxy(String clientProxy) {
		this.clientProxy = clientProxy;
	}

	public String getClientCookieStoreClass() {
		return clientCookieStoreClass;
	}

	public void setClientCookieStoreClass(String clientCookieStoreClass) {
		this.clientCookieStoreClass = clientCookieStoreClass;
	}

	public String getClientCredentialsProviderClass() {
		return clientCredentialsProviderClass;
	}

	public void setClientCredentialsProviderClass(String clientCredentialsProviderClass) {
		this.clientCredentialsProviderClass = clientCredentialsProviderClass;
	}

	public Integer getClientMaxRetryCount() {
		return clientMaxRetryCount;
	}

	public void setClientMaxRetryCount(Integer clientMaxRetryCount) {
		this.clientMaxRetryCount = clientMaxRetryCount;
	}

}
