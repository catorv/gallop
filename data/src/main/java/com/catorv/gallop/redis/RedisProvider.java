package com.catorv.gallop.redis;

import com.catorv.gallop.inject.Namespace;
import com.catorv.gallop.log.LoggerFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import redis.clients.jedis.JedisPool;

/**
 * Redis对象生成器
 * Created by cator on 8/2/16.
 */
@Singleton
public class RedisProvider implements Provider<Redis> {

	private JedisTemplate jedisTemplate;
	private Redis redis;

	@Inject
	public RedisProvider(@Namespace String namespace, JedisPool jedisPool,
	                     LoggerFactory loggerFactory) {
		Logger logger = loggerFactory.getLogger(JedisTemplate.class);
		jedisTemplate = new JedisTemplate(namespace, jedisPool, logger);
	}

	@Override
	public synchronized Redis get() {
		if (redis != null) {
			return redis;
		}

		redis = new Redis(jedisTemplate);
		return redis;
	}

}

