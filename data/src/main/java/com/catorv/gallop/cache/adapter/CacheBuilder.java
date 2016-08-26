package com.catorv.gallop.cache.adapter;

import com.catorv.gallop.cache.Cache;
import com.catorv.gallop.cache.CacheConfiguration;

/**
 * Cache Builder
 * Created by cator on 8/3/16.
 */
public interface CacheBuilder<K, V> {

	public Cache<K, V> buildCache(CacheConfiguration configuration);

	public boolean isConfiged();

}

