package com.catorv.gallop.service;

import com.catorv.gallop.httpclient.HttpClientExecutor;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Created by cator on 25/10/2016.
 */
public class HttpRemoteService implements RemoteService {

	@Inject
	private Provider<HttpClientExecutor> httpClientExecutorProvider;

	protected HttpClientExecutor getHttpClientExecutor() {
		return httpClientExecutorProvider.get();
	}

}
