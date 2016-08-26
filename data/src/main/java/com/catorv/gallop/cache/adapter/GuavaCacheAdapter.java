package com.catorv.gallop.cache.adapter;

import com.catorv.gallop.cache.Cache;

import java.util.Map;

/**
 * Guava Cache Adapter
 * Created by cator on 8/3/16.
 */
public class GuavaCacheAdapter<K, V> implements Cache<K, V> {

	private com.google.common.cache.Cache<K, V> adaptee;

	public GuavaCacheAdapter(com.google.common.cache.Cache<K, V> cache) {
		this.adaptee = cache;
	}

	@Override
	public V get(K key) {
		return adaptee.getIfPresent(key);
	}

	@Override
	public Map<K, V> getAll(Iterable<? extends K> keys) {
		return adaptee.getAllPresent(keys);
	}

	@Override
	public void invalidateAll(Iterable<? extends K> keys) {
		adaptee.invalidateAll(keys);
	}

	@Override
	public void put(K key, V value) {
		if (key == null) {
			return;
		}
		adaptee.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> values) {
		adaptee.putAll(values);
	}

	@Override
	public void invalidate(K key) {
		adaptee.invalidate(key);
	}

	@Override
	public void invalidateAll() {
		adaptee.invalidateAll();
	}

}

