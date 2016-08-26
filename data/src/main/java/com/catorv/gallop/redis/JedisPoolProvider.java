package com.catorv.gallop.redis;

import com.catorv.gallop.cfg.ConfigurationBuilder;
import com.catorv.gallop.inject.Namespace;
import com.catorv.gallop.lifecycle.Destroy;
import com.catorv.gallop.log.InjectLogger;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

/**
 * Redis连接池对象生成器
 * Created by cator on 8/2/16.
 */
@Singleton
public class JedisPoolProvider implements Provider<JedisPool> {

	private String namespace;

	private ConfigurationBuilder configBuilder;

	private JedisPool pool;

	@InjectLogger
	private Logger logger;

	@Inject
	public JedisPoolProvider(@Namespace String namespace,
	                         ConfigurationBuilder configBuilder) {
		this.namespace = namespace;
		this.configBuilder = configBuilder;
	}

	@Destroy
	public void shutdown() throws IOException {
		if (pool != null) {
			pool.destroy();
		}
	}

	@Override
	public synchronized JedisPool get() {
		if (pool != null) {
			return pool;
		}

		RedisConfiguration config = configBuilder.build(namespace,
				RedisConfiguration.class);

		if (Strings.isNullOrEmpty(config.getHost())) {
			config = configBuilder.build("redis", RedisConfiguration.class);
		}

		if (Strings.isNullOrEmpty(config.getHost())) {
			return null;
		}
		int port = config.getPort();
		if (port <= 0) {
			port = 6379;
		}

		int timeout = config.getTimeout();
		if (timeout <= 0) {
			timeout = 2000;
		}

		pool = new JedisPool(getJedisPoolConfig(config),
				config.getHost(), port, timeout, config.getPassword(),
				config.getDatabase(), config.getClientName());

		logger.info("redis: connect to {}:{}", config.getHost(), port);

		return pool;
	}

	private JedisPoolConfig getJedisPoolConfig(RedisConfiguration config) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		if (config.getMaxIdle() > 0) {
			poolConfig.setMaxIdle(config.getMaxIdle());
		}
		if (config.getMinIdle() > 0) {
			poolConfig.setMinIdle(config.getMinIdle());
		}
		if (config.getMaxTotal() > 0) {
			poolConfig.setMaxTotal(config.getMaxTotal());
		}
		return poolConfig;
	}
}

