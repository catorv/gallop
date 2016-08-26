package com.catorv.gallop.cache.adapter;

import com.catorv.gallop.cache.Cache;
import com.catorv.gallop.cache.CacheConfiguration;
import com.catorv.gallop.inject.Namespace;
import com.catorv.gallop.log.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import org.slf4j.Logger;
import redis.clients.jedis.JedisPool;

/**
 * Redis Client Adapter Builder
 * Created by cator on 8/3/16.
 */
public class RedisClientAdapterBuilder<V> implements CacheBuilder<String, V> {

	private JedisPool jedisPool;

	private ObjectMapper objectMapper;

	private Logger logger;

	private String namespace;

	@Inject
	public RedisClientAdapterBuilder(JedisPool jedisPool,
	                                 @Namespace String namespace,
	                                 ObjectMapper objectMapper,
	                                 LoggerFactory loggerFactory) {
		this.jedisPool = jedisPool;
		this.objectMapper = objectMapper;
		this.logger = loggerFactory.getLogger(RedisClientAdapter.class);
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
		return new RedisClientAdapter<>(jedisPool, objectMapper, region,
				config.getExpireSeconds(), logger);
	}

	@Override
	public boolean isConfiged() {
		return jedisPool != null;
	}
}

