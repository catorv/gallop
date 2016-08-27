package com.catorv.test.gallop.cache;

import com.catorv.gallop.cache.Cache;
import com.catorv.gallop.cache.CacheModule;
import com.catorv.gallop.cache.Cacheable;
import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.test.gallop.cache.model.CacheValueBean;
import com.catorv.gallop.json.JsonModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.memcached.MemcachedModule;
import com.catorv.gallop.redis.RedisModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

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
public class CacheTest {

	@Cacheable(region = "test")
	private Cache<String, Object> cache;

	@Test
	public void test() {
		cache.put("name", "ting");
		Assert.assertEquals("ting", cache.get("name"));

		CacheValueBean bean = new CacheValueBean();
		bean.setName("xiaoting");
		bean.setAge(1);
		bean.setBirthday(new Date());
		cache.put("person", bean);

		CacheValueBean bean2 = (CacheValueBean) cache.get("person");
		Assert.assertEquals(bean, bean2);

		cache.invalidate("name");

		Assert.assertNull(cache.get("name"));

		cache.invalidateAll();
		Assert.assertNull(cache.get("person"));
	}

}
