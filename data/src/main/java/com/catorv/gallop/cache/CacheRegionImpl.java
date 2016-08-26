package com.catorv.gallop.cache;

import com.catorv.gallop.cache.CacheEvent.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The implementation of CacheRegion
 * Created by cator on 8/3/16.
 */
public class CacheRegionImpl<K, V> implements CacheRegion<K, V> {

	private Map<Type, List<CacheEventListener>> eventListeners = new ConcurrentHashMap<>();

	private CacheStats cacheStats = new CacheStats();

	private Cache<K, V> cache;

	private String regionName;

	private CacheProvider cacheProvider;

	public CacheRegionImpl(String regionName, Cache<K, V> cache,
	                       CacheProvider cacheProvider) {
		this.regionName = regionName;
		this.cache = cache;
		this.cacheProvider = cacheProvider;
	}

	@Override
	public V get(K key) {
		if (key == null) {
			return null;
		}
		V value = cache.get(key);
		queueEvent(new CacheEvent<>(Type.GET, key, value));
		cacheStats.incrAccess(1, value != null);
		return value;
	}

	@Override
	public Map<K, V> getAll(Iterable<? extends K> keys) {
		if (keys == null) {
			return Collections.emptyMap();
		}

		Map<K, V> values = cache.getAll(keys);
		for (K key : keys) {
			V value = values.get(key);
			queueEvent(new CacheEvent<>(Type.GET, key, value));
			cacheStats.incrAccess(1, value != null);
		}
		return values;
	}

	@Override
	public void put(K key, V value) {
		if (key == null) {
			return;
		}
		cache.put(key, value);
		queueEvent(new CacheEvent<>(Type.PUT, key, value));
		cacheStats.incrUpdate(1);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> values) {
		if (values == null) {
			return;
		}
		cache.putAll(values);
		for (K key : values.keySet()) {
			queueEvent(new CacheEvent<K, V>(Type.PUT, key, values.get(key)));
		}
		// FIXME may have bugs due to null value handling.
		cacheStats.incrUpdate(values.size());
	}

	@Override
	public void invalidate(K key) {
		if (key == null) {
			return;
		}
		cache.invalidate(key);
		queueEvent(new CacheEvent<K, V>(Type.INVALIDATE, key, null));
		cacheStats.incrInvalidate(1);
	}

	@Override
	public void invalidateAll(Iterable<? extends K> keys) {
		if (keys == null) {
			return;
		}
		cache.invalidateAll(keys);
		for (K key : keys) {
			queueEvent(new CacheEvent<K, V>(Type.INVALIDATE, key, null));
			cacheStats.incrInvalidate(1);
		}
	}

	@Override
	public void invalidateAll() {
		cache.invalidateAll();
		queueEvent(new CacheEvent<K, V>(Type.INVALIDATE, null, null));
		cacheStats.incrInvalidateAll();
	}

	@Override
	public void addEventListener(Type eventType, CacheEventListener listener) {
		List<CacheEventListener> listeners = this.eventListeners.get(eventType);
		if (listeners == null) {
			listeners = new ArrayList<>();
			eventListeners.put(eventType, listeners);
		}
		listeners.add(listener);
	}

	@Override
	public void removeEventListener(Type eventType, CacheEventListener listener) {
		List<CacheEventListener> listeners = this.eventListeners.get(eventType);
		if (listeners == null) {
			return;
		}
		listeners.remove(listener);
	}

	@Override
	public CacheStats getStats() {
		return cacheStats;
	}

	private void queueEvent(CacheEvent<K, V> event) {
		List<CacheEventListener> listeners = eventListeners.get(event.getType());
		if (listeners == null) {
			return;
		}
		for (CacheEventListener listener : listeners) {
			listener.onEvent(event);
		}
	}

	@Override
	public String getRegionName() {
		return regionName;
	}

	@Override
	public CacheProvider getCacheProvider() {
		return cacheProvider;
	}
}

