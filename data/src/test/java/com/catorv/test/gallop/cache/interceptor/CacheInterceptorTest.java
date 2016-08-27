package com.catorv.test.gallop.cache.interceptor;

import com.catorv.gallop.cache.CacheManager;
import com.catorv.gallop.cache.CacheModule;
import com.catorv.gallop.cache.CacheStats;
import com.catorv.gallop.cache.Cacheable;
import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.json.JsonModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.memcached.MemcachedModule;
import com.catorv.gallop.redis.RedisModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.After;
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
public class CacheInterceptorTest {

	@Inject
	private CacheManager<String, Object> cacheManager;

	@Inject
	private CacheableObject cacheableObject;

	@After
	public void clearCache() {
		cacheManager.invalidateAll();
	}

	@Test
	public void testCacheableMethod() {
		for (int i = 0; i < 10; i++) {
			this.cacheMethod();
		}
		for (int i = 0; i < 10; i++) {
			this.cacheMethod2();
		}

		CacheStats stats = cacheManager.getCacheStats("test3");
		Assert.assertEquals(9, stats.getHitCount());
		Assert.assertEquals(1, stats.getMissCount());
		Assert.assertEquals(10, stats.getAccessCount());

		stats = cacheManager.getCacheStats("onthefly");
		Assert.assertEquals(9, stats.getHitCount());
		Assert.assertEquals(1, stats.getMissCount());
		Assert.assertEquals(10, stats.getAccessCount());
	}

	@Test
	public void testCacheableClass() {
		for (int i = 0; i < 10; i++) {
			cacheableObject.cacheMethod();
		}
		for (int i = 0; i < 10; i++) {
			cacheableObject.cacheMethod2();
		}

		CacheStats stats = cacheManager.getCacheStats("test3");
		Assert.assertEquals(18, stats.getHitCount());
		Assert.assertEquals(2, stats.getMissCount());
		Assert.assertEquals(20, stats.getAccessCount());
	}

	@Cacheable(region = "test3")
	public String cacheMethod() {
		return "abc";
	}

	@Cacheable(region = "onthefly")
	public String cacheMethod2() {
		return "abc";
	}

	@Cacheable(region = "test3")
	static class CacheableObject {

		public String cacheMethod() {
			return "abc";
		}

		public String cacheMethod2() {
			return "abc";
		}

	}
}
