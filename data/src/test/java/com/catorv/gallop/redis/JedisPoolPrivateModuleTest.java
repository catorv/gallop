package com.catorv.gallop.redis;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.inject.Namespace;
import com.catorv.gallop.inject.PrivateNamedModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
public class JedisPoolPrivateModuleTest extends AbstractModule {

	@Inject
	@Namespace("default")
	private JedisPool jp1;

	@Inject
	@Namespace("test")
	private JedisPool jp2;

	static class TestPrivateModule extends PrivateNamedModule {

		protected TestPrivateModule(String name) {
			super(name);
		}

		@Override
		protected void configure() {
			install(new ConfigurationModule(name));
			install(new RedisModule(name));

			expose(JedisPool.class).annotatedWith(getNamespace());
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

		Jedis jpr1 = jp1.getResource();
		Jedis jpr2 = jp2.getResource();

		Assert.assertNotEquals(jp1, jp2);

		Assert.assertNull(jpr1.get(key));
		jpr1.set(key, value);
		Assert.assertNotNull(jpr1.get(key));
		Assert.assertEquals(value, jpr1.get(key));

		Assert.assertNull(jpr2.get(key));
		jpr2.set(key, value);
		Assert.assertNotNull(jpr2.get(key));
		Assert.assertEquals(value, jpr2.get(key));

		jpr1.del(key);
		jpr2.del(key);

		jpr1.close();
		jpr2.close();
	}

}

