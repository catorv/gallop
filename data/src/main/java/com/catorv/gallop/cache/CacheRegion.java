package com.catorv.gallop.cache;

import com.catorv.gallop.cache.CacheEvent.Type;

/**
 * Cache Region
 * Created by cator on 8/3/16.
 */
public interface CacheRegion<K, V> extends Cache<K, V> {

	public void addEventListener(Type eventType, CacheEventListener listener);

	public void removeEventListener(Type eventType, CacheEventListener listener);

	public CacheStats getStats();

	public String getRegionName();

	public CacheProvider getCacheProvider();
}

