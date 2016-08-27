package com.catorv.test.gallop.cache.adapter;

import com.catorv.gallop.cache.Cache;
import com.catorv.gallop.cache.CacheConfiguration;
import com.catorv.gallop.cache.adapter.GuavaCacheAdapterBuilder;
import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.runner.RunWith;

/**
 *
 * Created by cator on 8/4/16.
 */
@RunWith(GuiceTestRunner.class)
public class GuavaCacheAdapterTest extends CacheAdapterTestBase {

	@Inject
	private GuavaCacheAdapterBuilder<String, Object> guavaCacheAdapterBuilder;

	@Override
	protected void configure() {
		try {
			this.install(new ConfigurationModule());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Cache<String, Object> getCache(CacheConfiguration configuration) {
		return guavaCacheAdapterBuilder.buildCache(configuration);
	}
}
