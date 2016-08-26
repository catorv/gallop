package com.catorv.gallop.cache;

/**
 * Created by cator on 8/3/16.
 */
public interface CacheEventListener {

	public <K, V> void onEvent(CacheEvent<K, V> event);

}

