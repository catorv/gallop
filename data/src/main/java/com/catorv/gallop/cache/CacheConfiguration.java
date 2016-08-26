package com.catorv.gallop.cache;

/**
 * Cache Configuration
 * Created by cator on 8/3/16.
 */
public class CacheConfiguration {

	private int maximumSize;

	private long expireSeconds;

	private String regionName;

	private String provider;

	public int getMaximumSize() {
		return maximumSize;
	}

	public void setMaximumSize(int maximumSize) {
		this.maximumSize = maximumSize;
	}

	public long getExpireSeconds() {
		return expireSeconds;
	}

	public void setExpireSeconds(long expireSeconds) {
		this.expireSeconds = expireSeconds;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getProvider() {
		return provider == null ? CacheProvider.LOCAL.getName() : provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
}

