package com.catorv.gallop.memcached;

import com.catorv.gallop.cfg.ConfigurationBuilder;
import com.catorv.gallop.inject.Namespace;
import com.catorv.gallop.lifecycle.Destroy;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Memcached Client Provider
 * Created by cator on 8/2/16.
 */
@Singleton
public class MemcachedClientProvider implements Provider<MemcachedClient> {

	/** 配置文件对象生成器 */
	private ConfigurationBuilder configBuilder;

	/** 命名空间,用作读取配置项时候的建名前缀 */
	private String namespace;

	private MemcachedClient client;

	@Inject
	public MemcachedClientProvider(@Namespace String namespace,
	                               ConfigurationBuilder configBuilder) {
		this.namespace = namespace;
		this.configBuilder = configBuilder;
	}

	@Destroy
	public synchronized void shutdownMemcached() throws IOException {
		if (client != null && !client.isShutdown()) {
			client.shutdown();
		}
	}

	@Override
	public synchronized MemcachedClient get() {
		if (client != null) {
			return client;
		}

		MemcachedConfiguration config = configBuilder.build("memcached",
				MemcachedConfiguration.class);

		String addresses = config.getAddresses();
		if (Strings.isNullOrEmpty(addresses)) {
			return null;
		}

		List<InetSocketAddress> addressList = AddrUtil.getAddresses(addresses);
		XMemcachedClientBuilder builder = new XMemcachedClientBuilder(addressList,
				getWeights(addressList.size(), config));
		try {
			client = builder.build();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return client;
	}

	private int[] getWeights(int count, MemcachedConfiguration config) {
		int[] ws = new int[count];
		int index = 0;
		String weights = config.getWeights();
		if (!Strings.isNullOrEmpty(weights)) {
			String[] parts = weights.split("\\s");

			while (count > 0 && index < parts.length) {
				index = ws.length - (count--);
				ws[index] = Integer.parseInt(parts[index]);
			}
		}
		while (count > 0) {
			ws[ws.length - (count--)] = 1;
		}
		return ws;
	}
}

