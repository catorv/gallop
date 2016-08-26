package com.catorv.gallop.cache.adapter;

import com.catorv.gallop.cache.Cache;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientCallable;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * MemcachedClient Adapter
 * Created by cator on 8/3/16.
 */
public class MemcachedClientAdapter<V> implements Cache<String, V> {

	private Logger logger = null;

	private MemcachedClient adaptee;

	private String namespace;

	private int valueExpireSeconds;

	public MemcachedClientAdapter(MemcachedClient memcachedClient,
	                              String namespace, long valueExpireSeconds,
	                              Logger logger) {
		this.adaptee = memcachedClient;
		this.namespace = namespace;
		this.valueExpireSeconds = (int) valueExpireSeconds;
		this.logger = logger;
	}

	@Override
	public V get(final String key) {
		return (new MemcachedClientInvokeTemplate() {
			@Override
			protected V invoke(MemcachedClient memcached, String key, V value)
					throws TimeoutException, InterruptedException, MemcachedException {
				return memcached.get(key);
			}
		}).execute(key, null);
	}

	@Override
	public Map<String, V> getAll(final Iterable<? extends String> keys) {
		return (new MemcachedClientInvokeTemplate() {
			@Override
			protected V invoke(MemcachedClient memcached, String key, V value)
					throws TimeoutException, InterruptedException, MemcachedException {
				return memcached.get(key);
			}
		}).execute(keys);
	}

	@Override
	public void put(final String key, final V value) {
		(new MemcachedClientInvokeTemplate() {
			@Override
			protected V invoke(MemcachedClient memcached, String key, V value)
					throws TimeoutException, InterruptedException, MemcachedException {
				memcached.set(key, valueExpireSeconds, value);
				return null;
			}
		}).execute(key, value);

	}

	@Override
	public void putAll(final Map<? extends String, ? extends V> values) {
		(new MemcachedClientInvokeTemplate() {
			@Override
			protected V invoke(MemcachedClient memcached, String key, V value)
					throws TimeoutException, InterruptedException, MemcachedException {
				memcached.set(key, valueExpireSeconds, values.get(key));
				return null;
			}
		}).execute(values);
	}

	@Override
	public void invalidate(final String key) {
		(new MemcachedClientInvokeTemplate() {
			@Override
			protected V invoke(MemcachedClient memcached, String key, V value)
					throws TimeoutException, InterruptedException, MemcachedException {
				memcached.delete(key);
				return null;
			}
		}).execute(key, null);
	}

	@Override
	public void invalidateAll(Iterable<? extends String> keys) {
		(new MemcachedClientInvokeTemplate() {
			@Override
			protected V invoke(MemcachedClient memcached, String key, V value)
					throws TimeoutException, InterruptedException, MemcachedException {
				memcached.delete(key);
				return null;
			}
		}).execute(keys);
	}

	@Override
	public void invalidateAll() {
		if (adaptee == null) {
			return;
		}
		try {
			// Reset namespace value to fix MemcachedClient Bug
//			String nsKey = "namespace:" + namespace;
//			Object ns = adaptee.get(nsKey);
//			if (ns instanceof String) {
//				adaptee.set(nsKey, valueExpireSeconds, Long.valueOf((String) ns));
//			}

			adaptee.invalidateNamespace(namespace);
		} catch (MemcachedException | InterruptedException e) {
			logger.error("Memcached operation error", e);
		} catch (TimeoutException e) {
			logger.error("Memcached operation timeout", e);
		}
	}

	private abstract class MemcachedClientInvokeTemplate {
		void execute(Map<? extends String, ? extends V> values) {
			for (String key : values.keySet()) {
				execute0(adaptee, key, values.get(key));
			}
		}

		Map<String, V> execute(Iterable<? extends String> keys) {
			Map<String, V> rets = new HashMap<String, V>();
			for (String key : keys) {
				rets.put(key, execute0(adaptee, key, null));
			}
			return rets;
		}

		V execute(String key, V value) {
			return execute0(adaptee, key, value);
		}

		V execute0(MemcachedClient memcached, final String key, final V value) {
			if (memcached == null) {
				return null;
			}
			try {
				return memcached.withNamespace(namespace,
						new MemcachedClientCallable<V>() {

							@Override
							public V call(MemcachedClient adaptee) throws MemcachedException,
									InterruptedException, TimeoutException {
								return invoke(adaptee, key, value);
							}
						});
			} catch (TimeoutException e) {
				logger.error("Memcached operation timeout", e);
			} catch (InterruptedException | MemcachedException e) {
				logger.error("Memcached operation error", e);
			}
			return null;
		}

		protected abstract V invoke(MemcachedClient memcached, String key, V value)
				throws TimeoutException, InterruptedException, MemcachedException;
	}
}

