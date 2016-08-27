package com.catorv.test.gallop.memcached;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.inject.Namespace;
import com.catorv.gallop.inject.PrivateNamedModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.memcached.MemcachedModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import net.rubyeye.xmemcached.MemcachedClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing for memcached name
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
public class MemcachedPrivateModuleTest extends AbstractModule {

	@Inject
	@Namespace("default")
	private MemcachedClient mc1;

	@Inject
	@Namespace("test")
	private MemcachedClient mc2;

	protected static class TestPrivateModule extends PrivateNamedModule {
		TestPrivateModule(String name) {
			super(name);
		}

		@Override
		protected void configure() {
			install(new ConfigurationModule(name));
			install(new MemcachedModule(name));

			exposeNamespaceAnnotated(MemcachedClient.class);
		}
	}

	@Override
	protected void configure() {
		install(new LifecycleModule());
		install(new TestPrivateModule("default"));
		install(new TestPrivateModule("test"));
	}

	@Test
	public void testCache() throws Exception {
		String key = "key";
		String value = "abc";

		Assert.assertNotEquals(mc1, mc2);

		Assert.assertNull(mc1.get(key));
		mc1.set(key, 1, value);
		Assert.assertNotNull(mc1.get(key));
		Assert.assertEquals(value, mc1.get(key));

		Assert.assertNull(mc2.get(key));
		mc2.set(key, 1, value);
		Assert.assertNotNull(mc2.get(key));
		Assert.assertEquals(value, mc2.get(key));

		mc1.delete(key);
		mc2.delete(key);
	}
}

