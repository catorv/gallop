package com.catorv.test.gallop.cache;

import com.catorv.gallop.cache.Cache;
import com.catorv.gallop.cache.CacheManager;
import com.catorv.gallop.cache.CacheModule;
import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.inject.Namespace;
import com.catorv.gallop.inject.PrivateNamedModule;
import com.catorv.gallop.json.JsonModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.memcached.MemcachedModule;
import com.catorv.gallop.redis.RedisModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * Created by cator on 8/4/16.
 */
@RunWith(GuiceTestRunner.class)
public class CacheManagerPrivateModuleTest extends AbstractModule {

	@Inject
	@Namespace("default")
	private CacheManager<String, Object> cm1;

	@Inject
	@Namespace("test")
	private CacheManager<String, Object> cm2;

	@Test
	public void test() {
		Assert.assertNotNull(cm1);
		Assert.assertNotNull(cm2);
		Assert.assertNotEquals(cm1, cm2);

		Cache<String, Object> c1 = cm1.getCache("test");
		Cache<String, Object> c2 = cm2.getCache("test");

		String key = "key";
		String value = "abc";

		c1.put(key, value);
		Assert.assertNotNull(c1.get(key));
		Assert.assertEquals(value, c1.get(key));

		Assert.assertNull(c2.get(key));
		c2.put(key, value);
		Assert.assertNotNull(c2.get(key));
		Assert.assertEquals(value, c2.get(key));

		c1.invalidate(key);
		c2.invalidate(key);
	}

	@Override
	protected void configure() {
		install(new LifecycleModule());
		install(new LoggerModule());
		install(new JsonModule());
		install(new TestPrivateModule("default"));
		install(new TestPrivateModule("test"));
	}

	private static class TestPrivateModule extends PrivateNamedModule {

		TestPrivateModule(String name) {
			super(name);
		}

		@Override
		protected void configure() {
			install(new ConfigurationModule(name));
			install(new MemcachedModule(name));
			install(new RedisModule(name));
			install(new CacheModule(name));

			TypeLiteral<CacheManager<String, Object>> cacheManagerTypeLiteral;
			cacheManagerTypeLiteral = new TypeLiteral<CacheManager<String, Object>>() {};
			exposeNamespaceAnnotated(cacheManagerTypeLiteral);
		}
	}
}
