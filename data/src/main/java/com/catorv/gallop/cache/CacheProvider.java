package com.catorv.gallop.cache;

/**
 * CacheProvider
 * Created by cator on 8/3/16.
 */
public enum CacheProvider {
	LOCAL("local"),
	MEMCACHED("memcached"),
	REDIS("redis"),
	DUMMY("dummy");

	private String name;

	CacheProvider(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static CacheProvider[] allProviders = {LOCAL, MEMCACHED, REDIS, DUMMY};

	public static CacheProvider fromName(String name) {
		name = name.toLowerCase();
		for (CacheProvider provider : allProviders) {
			if (provider.getName().equals(name)) {
				return provider;
			}
		}
		System.err.println("[ERROR] incorrect cache provider for \"" + name + "\"");
		return DUMMY;
	}

}

