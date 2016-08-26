package com.catorv.gallop.cache.adapter;

import com.catorv.gallop.cache.Cache;
import com.catorv.gallop.cache.CacheConfiguration;
import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.memcached.MemcachedModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 *
 * Created by cator on 8/4/16.
 */
@RunWith(GuiceTestRunner.class)
public class MemcachedClientAdapterTest extends CacheAdapterTestBase {

	@Inject
	private MemcachedClientAdapterBuilder<Object> memcachedClientAdapterBuilder;

	@Override
	protected void configure() {
		try {
			this.install(new LoggerModule());
			this.install(new ConfigurationModule());
			this.install(new LifecycleModule());
			this.install(new MemcachedModule());
			this.bind(MemcachedClientAdapterBuilder.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Cache<String, Object> getCache(CacheConfiguration configuration) {
		return memcachedClientAdapterBuilder.buildCache(configuration);
	}
}
