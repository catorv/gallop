package com.catorv.gallop.memcached;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import net.rubyeye.xmemcached.MemcachedClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		ConfigurationModule.class,
		LifecycleModule.class,
		MemcachedModule.class
})
public class MemcachedTest {

	@Inject
	private MemcachedClient mc1;

	@Test
	public void testCache() throws Exception {
		String key = "key";
		String value = "abc";

		Assert.assertNull(mc1.get(key));
		mc1.set(key, 1, value);
		Assert.assertNotNull(mc1.get(key));
		Assert.assertEquals(value, mc1.get(key));

		mc1.delete(key);
	}
}

