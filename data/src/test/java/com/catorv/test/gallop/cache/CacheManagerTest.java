package com.catorv.test.gallop.cache;

import com.catorv.gallop.cache.Cache;
import com.catorv.gallop.cache.CacheManager;
import com.catorv.gallop.cache.CacheModule;
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
public class CacheManagerTest {

	@Inject
	private CacheManager<String, Object> cacheManager;

	@Test
	public void test() {
		Assert.assertNotNull(cacheManager);

		Cache<String, Object> c1 = cacheManager.getCache("test");

		String key = "key";
		String value = "abc";

		c1.put(key, value);
		Assert.assertNotNull(c1.get(key));
		Assert.assertEquals(value, c1.get(key));

		c1.invalidate(key);
	}

}
