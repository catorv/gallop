package com.catorv.gallop.cache;

import java.util.Map;

/**
 * 缓存对象
 * Created by cator on 8/3/16.
 */
public interface Cache<K, V> {

	/**
	 * GET cache value.
	 * @param key
	 * @return
	 */
	public V get(K key);

	public Map<K, V> getAll(Iterable<? extends K> keys);

	/**
	 * PUT value to cache.
	 * @param key
	 * @param value
	 */
	public void put(K key, V value);

	/**
	 * PUT multiple values to cache.
	 * @param values
	 */
	public void putAll(Map<? extends K, ? extends V> values);

	/**
	 * Remove key from cache.
	 * @param key
	 */
	public void invalidate(K key);

	public void invalidateAll(Iterable<? extends K> keys);

	/**
	 * Remove all keys from cache.
	 */
	public void invalidateAll();
}

