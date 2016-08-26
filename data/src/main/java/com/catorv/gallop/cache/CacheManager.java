package com.catorv.gallop.cache;

import java.util.concurrent.ConcurrentMap;

/**
 * Cache Manager
 * Created by cator on 8/3/16.
 */
public interface CacheManager<K, V> {

	public Cache<K, V> getCache(String regionName);

	public ConcurrentMap<String, Cache<K, V>> getAllCaches();

	public CacheStats getCacheStats(String regionName);

	public ConcurrentMap<String, CacheStats> getAllCacheStats();

	public void invalidate(String regionName);

	public void invalidateAll();
}

