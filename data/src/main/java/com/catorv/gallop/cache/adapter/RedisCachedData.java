package com.catorv.gallop.cache.adapter;

import net.rubyeye.xmemcached.transcoders.CachedData;

/**
 * Redis Cached Data
 * Created by cator on 8/3/16.
 */
public class RedisCachedData {

	private long createdTime;

	private CachedData cachedData;

	public RedisCachedData() {

	}

	public RedisCachedData(CachedData cachedData) {
		this(cachedData, System.currentTimeMillis());
	}

	public RedisCachedData(CachedData cachedData, long createdTime) {
		this.cachedData = cachedData;
		this.createdTime = createdTime;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public CachedData getCachedData() {
		return cachedData;
	}

	public void setCachedData(CachedData cachedData) {
		this.cachedData = cachedData;
	}

}

