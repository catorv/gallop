package com.catorv.gallop.cache.adapter;

import com.catorv.gallop.cache.CacheConfiguration;
import com.catorv.gallop.cache.Cache;

import java.util.concurrent.TimeUnit;

/**
 * Guava Cache Adapter Builder
 * Created by cator on 8/3/16.
 */
public class GuavaCacheAdapterBuilder<K, V> implements CacheBuilder<K, V> {

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public Cache<K, V> buildCache(CacheConfiguration configuration) {
		com.google.common.cache.CacheBuilder cacheBuilder;
		cacheBuilder = com.google.common.cache.CacheBuilder.newBuilder()
				.concurrencyLevel(10)
				.initialCapacity(16);

		int maximumSize = configuration.getMaximumSize();
		if (maximumSize <= 0) {
			cacheBuilder.maximumSize(2048);
		} else {
			cacheBuilder.maximumSize(maximumSize);
		}

		long expireSeconds = configuration.getExpireSeconds();
		if (expireSeconds > 0) {
			cacheBuilder.expireAfterWrite(expireSeconds, TimeUnit.SECONDS);
		}

		return new GuavaCacheAdapter(cacheBuilder.build());
	}

	@Override
	public boolean isConfiged() {
		return true;
	}
}

