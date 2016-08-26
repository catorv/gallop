package com.catorv.gallop.cache.adapter;

import com.catorv.gallop.cache.CacheConfiguration;
import com.catorv.gallop.cache.Cache;
import com.catorv.gallop.inject.Namespace;
import com.catorv.gallop.log.LoggerFactory;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import net.rubyeye.xmemcached.MemcachedClient;
import org.slf4j.Logger;

/**
 * MemcacheClient Adapter Builder
 * Created by cator on 8/3/16.
 */
public class MemcachedClientAdapterBuilder<V> implements CacheBuilder<String, V> {

	private MemcachedClient memcachedClient;
	private String namespace;
	private Logger logger;

	@Inject
	public MemcachedClientAdapterBuilder(MemcachedClient memcachedClient,
	                                     @Namespace String namespace,
	                                     LoggerFactory loggerFactory) {
		this.memcachedClient = memcachedClient;
		this.logger = loggerFactory.getLogger(MemcachedClientAdapter.class);
		this.namespace = namespace;
	}

	@Override
	public Cache<String, V> buildCache(CacheConfiguration config) {
		String region = config.getRegionName();
		if (!Strings.isNullOrEmpty(namespace)) {
			region = namespace + ".cache." + region;
		} else {
			region = "cache." + region;
		}
		return new MemcachedClientAdapter<>(memcachedClient, region,
				config.getExpireSeconds(), logger);
	}

	@Override
	public boolean isConfiged() {
		return memcachedClient != null;
	}

}

