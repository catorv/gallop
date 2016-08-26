package com.catorv.gallop.cache;

import java.util.Map;

/**
 * Dummy Cache
 * Created by cator on 8/3/16.
 */
public class DummyCache<K, V> implements Cache<K, V> {

	@Override
	public V get(K key) {
		return null;
	}

	@Override
	public Map<K, V> getAll(Iterable<? extends K> keys) {
		return null;
	}

	@Override
	public void put(K key, V value) {
		// nothing
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> values) {
		// nothing
	}

	@Override
	public void invalidate(K key) {
		// nothing
	}

	@Override
	public void invalidateAll(Iterable<? extends K> keys) {
		// nothing
	}

	@Override
	public void invalidateAll() {
		// nothing
	}

}
