package com.catorv.gallop.redis;

import com.catorv.gallop.inject.AbstractNamedModule;
import redis.clients.jedis.JedisPool;

/**
 * Redis Module
 * 依赖的模块:
 *  LoggerModule
 *  ConfigurationModule
 *  LifecycleModule
 * Created by cator on 8/2/16.
 */
public class RedisModule extends AbstractNamedModule {

	public RedisModule() {
		this(null);
	}

	public RedisModule(String name) {
		super(name);
	}

	@Override
	protected void configure() {
		if (name != null) {
			bindNamespaceAnnotated(JedisPool.class).toProvider(JedisPoolProvider.class);
			bindNamespaceAnnotated(Redis.class).toProvider(RedisProvider.class);
		}
		bind(JedisPool.class).toProvider(JedisPoolProvider.class);
		bind(Redis.class).toProvider(RedisProvider.class);
	}

}

