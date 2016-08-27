package com.catorv.test.gallop.cache.adapter;

import com.catorv.gallop.cache.Cache;
import com.catorv.gallop.cache.CacheConfiguration;
import com.catorv.gallop.cache.adapter.RedisClientAdapterBuilder;
import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.json.JsonModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.redis.RedisModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 *
 * Created by cator on 8/4/16.
 */
@RunWith(GuiceTestRunner.class)
public class RedisClientAdapterTest extends CacheAdapterTestBase {

	@Inject
	private RedisClientAdapterBuilder<Object> redisClientAdapterBuilder;

	@Override
	protected void configure() {
		try {
			this.install(new LoggerModule());
			this.install(new ConfigurationModule());
			this.install(new JsonModule());
			this.install(new RedisModule());
			this.bind(RedisClientAdapterBuilder.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Cache<String, Object> getCache(CacheConfiguration configuration) {
		return redisClientAdapterBuilder.buildCache(configuration);
	}
}
