package com.catorv.test.gallop.redis;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.redis.Redis;
import com.catorv.gallop.redis.RedisModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		LoggerModule.class,
		LifecycleModule.class,
		ConfigurationModule.class,
		RedisModule.class
})
public class RedisTest {

	@Inject
	public Redis redis;

	@Test
	public void test() {
		String key = "key";
		String value = "abc";

		redis.set(key, value);
		Assert.assertNotNull(redis.get(key));
		Assert.assertEquals(value, redis.get(key));

		redis.del(key);
	}

}
