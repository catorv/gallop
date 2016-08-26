package com.catorv.gallop.memcached;

import com.catorv.gallop.inject.AbstractNamedModule;
import com.google.inject.Inject;
import net.rubyeye.xmemcached.MemcachedClient;

import java.util.Properties;

/**
 * Mecached Module
 * 依赖模块:
 *  ConfigurationModule
 *  LifecycleModule
 * Created by cator on 8/2/16.
 */
public class MemcachedModule extends AbstractNamedModule {

	@Inject
	private Properties properties;

	public MemcachedModule() {
		this(null);
	}

	public MemcachedModule(String name) {
		super(name);
	}

	@Override
	protected void configure() {
		if (name != null) {
			bindNamespaceAnnotated(MemcachedClient.class).toProvider(MemcachedClientProvider.class);
		}
		bind(MemcachedClient.class).toProvider(MemcachedClientProvider.class);
	}

}

