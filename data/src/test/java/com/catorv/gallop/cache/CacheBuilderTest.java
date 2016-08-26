package com.catorv.gallop.cache;

import com.catorv.gallop.cache.adapter.CacheBuilder;
import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.json.JsonModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.memcached.MemcachedModule;
import com.catorv.gallop.redis.RedisModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * Created by cator on 8/4/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		LifecycleModule.class,
		ConfigurationModule.class,
		LoggerModule.class,
		JsonModule.class,
		MemcachedModule.class,
		RedisModule.class,
		CacheModule.class
})
public class CacheBuilderTest {

	@Inject
	private CacheBuilder<String, Object> cacheBuilder;

	@Test
	public void test() {
		Assert.assertNotNull(cacheBuilder);
	}

}
