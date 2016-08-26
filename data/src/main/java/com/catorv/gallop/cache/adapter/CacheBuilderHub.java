package com.catorv.gallop.cache.adapter;

import com.catorv.gallop.cache.Cache;
import com.catorv.gallop.cache.CacheConfiguration;
import com.catorv.gallop.cache.CacheProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Cache builder hub
 * Created by cator on 8/3/16.
 */
@Singleton
public class CacheBuilderHub implements CacheBuilder<String, Object> {

	@Inject
	private MemcachedClientAdapterBuilder<Object> memcachedClientAdapterBuilder;

	@Inject
	private GuavaCacheAdapterBuilder<String, Object> guavaCacheAdapterBuilder;

	@Inject
	private RedisClientAdapterBuilder<Object> redisClientAdapterBuilder;

	@Override
	public Cache<String, Object> buildCache(CacheConfiguration config) {
		CacheProvider provider = CacheProvider.fromName(config.getProvider());
		CacheBuilder<String, Object> builder = null;
		switch (provider) {
			case LOCAL:
				builder = guavaCacheAdapterBuilder;
				break;
			case MEMCACHED:
				builder = memcachedClientAdapterBuilder;
				break;
			case REDIS:
				builder = redisClientAdapterBuilder;
				break;
		}
		if (builder != null && builder.isConfiged()) {
			return builder.buildCache(config);
		}
		return null;
	}

	@Override
	public boolean isConfiged() {
		return true;
	}

}

