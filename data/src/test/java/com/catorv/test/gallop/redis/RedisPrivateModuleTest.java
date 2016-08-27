package com.catorv.test.gallop.redis;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.inject.Namespace;
import com.catorv.gallop.inject.PrivateNamedModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.redis.Redis;
import com.catorv.gallop.redis.RedisModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import redis.clients.jedis.JedisPool;

/**
 *
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
public class RedisPrivateModuleTest extends AbstractModule {

	@Inject
	@Namespace("default")
	private Redis redis1;

	@Inject
	@Namespace("test")
	private Redis redis2;

	static class TestPrivateModule extends PrivateNamedModule {

		protected TestPrivateModule(String name) {
			super(name);
		}

		@Override
		protected void configure() {
			install(new ConfigurationModule(name));
			install(new RedisModule(name));

			exposeNamespaceAnnotated(JedisPool.class);
			exposeNamespaceAnnotated(Redis.class);
		}
	}

	@Override
	protected void configure() {
		install(new LifecycleModule());
		install(new LoggerModule());
		install(new TestPrivateModule("default"));
		install(new TestPrivateModule("test"));
	}

	@Test
	public void test() {

		String key = "key";
		String value = "abc";

		Assert.assertNotEquals(redis1, redis2);

		Assert.assertNull(redis1.get(key));
		redis1.set(key, value);
		Assert.assertNotNull(redis1.get(key));
		Assert.assertEquals(value, redis1.get(key));

		Assert.assertNull(redis2.get(key));
		redis2.set(key, value);
		Assert.assertNotNull(redis2.get(key));
		Assert.assertEquals(value, redis2.get(key));

		redis1.del(key);
		redis2.del(key);
	}

}

