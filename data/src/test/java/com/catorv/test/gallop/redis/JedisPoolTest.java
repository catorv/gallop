package com.catorv.test.gallop.redis;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.redis.RedisModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
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
@GuiceModule({
		LoggerModule.class,
		ConfigurationModule.class,
		LifecycleModule.class,
		RedisModule.class
})
public class JedisPoolTest {

	@Inject
	private JedisPool jp1;

	@Test
	public void test() {
		String key = "key";
		String value = "abc";

		Jedis jpr1 = jp1.getResource();


		jpr1.set(key, value);
		Assert.assertNotNull(jpr1.get(key));
		Assert.assertEquals(value, jpr1.get(key));

		jpr1.del(key);

		jpr1.close();
	}

}

