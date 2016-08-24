package com.catorv.gallop.httpclient;

import com.google.inject.Inject;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * HttpClient Executor
 * Created by cator on 8/15/16.
 */
public class HttpClientExecutor {

	@Inject
	private CloseableHttpClient httpClient;

	@Inject
	private RequestConfig defaultRequestConfig;

	private String url;
	private HttpRequestBase request;
	private HttpContext context;

	private Charset charset = Charset.defaultCharset();

	private String stringBody;
	private UrlEncodedFormEntity formBody;
	private MultipartEntityBuilder multipartBody;

	public Builder get(String url) throws URISyntaxException {
		return getBuilder(url, new HttpGet());
	}

	public Builder post(String url) throws URISyntaxException {
		return getBuilder(url, new HttpPost());
	}

	public Builder delete(String url) throws URISyntaxException {
		return getBuilder(url, new HttpDelete());
	}

	public Builder put(String url) throws URISyntaxException {
		return getBuilder(url, new HttpPut());
	}

	public Builder head(String url) throws URISyntaxException {
		return getBuilder(url, new HttpHead());
	}

	public Builder trace(String url) throws URISyntaxException {
		return getBuilder(url, new HttpTrace());
	}

	public Builder options(String url) throws URISyntaxException {
		return getBuilder(url, new HttpOptions());
	}

	private Builder getBuilder(String url, HttpRequestBase request) throws URISyntaxException {
		this.url = url;
		this.request = request;
		this.context = new BasicHttpContext();
		return new Builder(this);
	}

	public Response execute() throws IOException {
		if (request instanceof HttpEntityEnclosingRequestBase) {
			HttpEntityEnclosingRequestBase r = (HttpEntityEnclosingRequestBase) request;
			if (stringBody != null) {
				StringEntity entity = new StringEntity(stringBody, Consts.UTF_8);
				Header header = request.getLastHeader(HttpHeaders.CONTENT_TYPE);
				if (header != null) {
					entity.setContentType(header);
				}
				r.setEntity(entity);
			} else if (formBody != null) {
				r.setEntity(formBody);
			} else if (multipartBody != null) {
				r.setEntity(multipartBody.build());
			}
		}
		return new Response(httpClient.execute(request, context));
	}

	/**
	 * HttpClientExecutor Builder
	 */
	public static class Builder {

		private HttpClientExecutor executor;
		private URIBuilder uriBuilder;
		private RequestConfig.Builder requestConfigBuilder;
		private CookieStore cookieStore;
		private boolean cookieSet = false;

		public Builder(HttpClientExecutor executor) throws URISyntaxException {
			this.executor = executor;
			uriBuilder = new URIBuilder(executor.url);
			cookieStore = new BasicCookieStore();
		}

		private RequestConfig.Builder getRequestConfigBuilder() {
			if (requestConfigBuilder == null) {
				requestConfigBuilder = RequestConfig.copy(executor.defaultRequestConfig);
			}
			return requestConfigBuilder;
		}

		public Builder charset(Charset charset) {
			executor.charset = charset;
			return this;
		}

		public Builder header(String name, String value) {
			executor.request.addHeader(name, value);
			return this;
		}

		public Builder updateHeader(String name, String value) {
			executor.request.setHeader(name, value);
			return this;
		}

		public Builder headers(Map<String, String> headers) {
			if (headers != null) {
				for (String name : headers.keySet()) {
					executor.request.addHeader(name, headers.get(name));
				}
			}
			return this;
		}

		public Builder cookie(String name, String value, String path,
		                      Date expiryDate, boolean secure) {
			BasicClientCookie cookie = new BasicClientCookie(name, value);
			if (path != null) {
				cookie.setPath(path);
			}
			if (expiryDate != null) {
				cookie.setExpiryDate(expiryDate);
			}
			cookie.setSecure(secure);
			cookieStore.addCookie(cookie);
			cookieSet = true;
			return this;
		}

		public Builder cookie(String name, String value, String path,
		                      Date expiryDate) {
			return cookie(name, value, path, expiryDate, false);
		}

		public Builder cookie(String name, String value, String path) {
			return cookie(name, value, path, null, false);
		}

		public Builder cookie(String name, String value) {
			return cookie(name, value, null, null, false);
		}

		public Builder contentType(String contentType) {
			return updateHeader(HttpHeaders.CONTENT_TYPE, contentType);
		}

		public Builder userAgent(String userAgent) {
			return updateHeader(HttpHeaders.USER_AGENT, userAgent);
		}

		public Builder json() {
			return contentType(ContentType.APPLICATION_JSON.toString());
		}

		public Builder formUrlencoded() {
			return contentType(ContentType.APPLICATION_FORM_URLENCODED.toString());
		}

		public Builder multipartFormData() {
			return contentType(ContentType.MULTIPART_FORM_DATA.toString());
		}

		public Builder parameter(String name, Object content) {
			uriBuilder.addParameter(name, String.valueOf(content));
			return this;
		}

		public Builder parameters(Map<String, Object> map) {
			if (map != null) {
				for (String name : map.keySet()) {
					parameter(name, map.get(name));
				}
			}
			return this;
		}

		public Builder body(String body) {
			executor.stringBody = body;
			return this;
		}

		public Builder formBody(Map<String, String> map) {
			List<NameValuePair> form = new ArrayList<>();
			if (map != null) {
				for (String name : map.keySet()) {
					form.add(new BasicNameValuePair(name, map.get(name)));
				}
			}
			return formBody(form);
		}

		public Builder formBody(List<NameValuePair> nameValuePairs) {
			return formBody(new UrlEncodedFormEntity(nameValuePairs, executor.charset));
		}

		public Builder formBody(UrlEncodedFormEntity formEntity) {
			executor.formBody = formEntity;
			return this;
		}

		public Builder multipartBody(String name, Object content) {
			if (executor.multipartBody == null) {
				executor.multipartBody = MultipartEntityBuilder.create();
			}
			if (content instanceof File) {
				executor.multipartBody.addPart(name, new FileBody((File) content));
			} else {
				executor.multipartBody.addTextBody(name, String.valueOf(content));
			}
			return this;
		}

		public Builder multipartBody(Map<String, Object> map) {
			if (map != null) {
				for (String name : map.keySet()) {
					multipartBody(name, map.get(name));
				}
			}
			return this;
		}

		public Builder expectContinueEnabled(boolean enabled) {
			getRequestConfigBuilder().setExpectContinueEnabled(enabled);
			return this;
		}

		public Builder proxy(HttpHost host) {
			getRequestConfigBuilder().setProxy(host);
			return this;
		}

		public Builder localAddress(InetAddress address) {
			getRequestConfigBuilder().setLocalAddress(address);
			return this;
		}

		public Builder cookieSpec(String spec) {
			getRequestConfigBuilder().setCookieSpec(spec);
			return this;
		}

		public Builder redirectsEnabled(boolean enabled) {
			getRequestConfigBuilder().setRedirectsEnabled(enabled);
			return this;
		}

		public Builder relativeRedirectsAllowed(boolean allowed) {
			getRequestConfigBuilder().setRelativeRedirectsAllowed(allowed);
			return this;
		}

		public Builder circularRedirectsAllowed(boolean allowed) {
			getRequestConfigBuilder().setCircularRedirectsAllowed(allowed);
			return this;
		}

		public Builder maxRedirects(int num) {
			getRequestConfigBuilder().setMaxRedirects(num);
			return this;
		}

		public Builder authenticationEnabled(boolean enabled) {
			getRequestConfigBuilder().setAuthenticationEnabled(enabled);
			return this;
		}

		public Builder targetPreferredAuthSchemes(Collection<String> schemes) {
			getRequestConfigBuilder().setTargetPreferredAuthSchemes(schemes);
			return this;
		}

		public Builder proxyPreferredAuthSchemes(Collection<String> schemes) {
			getRequestConfigBuilder().setProxyPreferredAuthSchemes(schemes);
			return this;
		}

		public Builder connectionRequestTimeout(int timeout) {
			getRequestConfigBuilder().setConnectionRequestTimeout(timeout);
			return this;
		}

		public Builder connectTimeout(int timeout) {
			getRequestConfigBuilder().setConnectTimeout(timeout);
			return this;
		}

		public Builder socketTimeout(int timeout) {
			getRequestConfigBuilder().setSocketTimeout(timeout);
			return this;
		}

		public Builder contentCompressionEnabled(boolean enabled) {
			getRequestConfigBuilder().setContentCompressionEnabled(enabled);
			return this;
		}

		public HttpClientExecutor build() throws URISyntaxException {
			executor.request.setURI(uriBuilder.setCharset(executor.charset).build());
			if (executor.stringBody == null) {
				if (executor.formBody != null) {
					formUrlencoded();
				} else if (executor.multipartBody != null) {
					multipartFormData();
				}
			}

			if (requestConfigBuilder != null) {
				executor.request.setConfig(requestConfigBuilder.build());
			}

			if (cookieSet) {
				executor.context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
			}

			return executor;
		}

		public Response execute() throws URISyntaxException, IOException {
			return build().execute();
		}

	}

	/**
	 * HttpClientExecutor Excute Result
	 */
	public static class Response {

		private CloseableHttpResponse response;
		private HttpEntity entity;

		public Response(CloseableHttpResponse response) {
			this.response = response;
			this.entity = response.getEntity();
		}

		public <T> T handleResponse(ResponseHandler<T> handler) throws IOException {
			try {
				return handler.handleResponse(response);
			} finally {
				close();
			}
		}

		public String returnString() throws IOException {
			try {
				return EntityUtils.toString(entity);
			} finally {
				close();
			}
		}

		public String returnString(Charset charset) throws IOException {
			try {
				return EntityUtils.toString(entity, charset);
			} finally {
				close();
			}
		}

		public byte[] returnBytes() throws IOException {
			try {
				return EntityUtils.toByteArray(entity);
			} finally {
				close();
			}
		}

		public void toFile(File file) throws IOException {
			try {
				entity.writeTo(new FileOutputStream(file));
			} finally {
				close();
			}
		}

		public void toOutputStream(OutputStream os) throws IOException {
			try {
				entity.writeTo(os);
			} finally {
				close();
			}
		}

		public void close() throws IOException {
			response.close();
		}
	}

}
