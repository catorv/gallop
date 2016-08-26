package com.catorv.gallop.cache;

import com.catorv.gallop.cache.adapter.CacheBuilder;
import com.catorv.gallop.cfg.ConfigurationBuilder;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Cache Manager Provider
 * Created by cator on 8/3/16.
 */
@Singleton
public class CacheManagerProvider implements CacheManager<String, Object>,
		Provider<CacheManager<String, Object>> {

	private CacheManager<String, Object> instance;

	@Inject
	private Properties properties;

	@Inject
	private ConfigurationBuilder configBuilder;

	@Inject
	private CacheBuilder<String, Object> cacheBuilder;

	private ConcurrentMap<String, CacheRegion<String, Object>> regions;

	private CacheRegion<String, Object> dummyRegion = new CacheRegionImpl<>(
			"dummy", new DummyCache<String, Object>(), CacheProvider.DUMMY);

	private boolean cacheEnabled = true;

	@Override
	public synchronized CacheManager<String, Object> get() {
		if (instance != null) {
			return instance;
		}
		buildCacheManager();
		instance = this;
		cacheEnabled = Boolean.valueOf(properties.getProperty("cache.enabled", "true"));
		return instance;
	}

	private void buildCacheManager() {
		for (String regionName : getConfigedRegionNames()) {
			CacheRegion<String, Object> buildCacheRegion = buildCacheRegion(regionName);
			if (buildCacheRegion == null) {
				continue;
			}
			regions.put(regionName, buildCacheRegion);
		}
	}

	private Set<String> getConfigedRegionNames() {
		Set<String> regionNames = new HashSet<>();
		regions = new ConcurrentHashMap<>();
		for (Object key : properties.keySet()) {
			String sKey = (String) key;
			if (sKey.startsWith("cache.")) {
				int index = sKey.indexOf(".", 6);
				if (index < 0) {
					continue;
				}
				regionNames.add(sKey.substring(6, index));
			}
		}
		return regionNames;
	}

	private CacheRegion<String, Object> buildCacheRegion(String regionName) {
		CacheConfiguration config = configBuilder.build("cache." + regionName,
				CacheConfiguration.class);

		config.setRegionName(regionName);
		if (config.getProvider() == null) {
			config.setProvider(CacheProvider.LOCAL.getName());
		}
		Cache<String, Object> buildCache = cacheBuilder.buildCache(config);
		if (buildCache == null) {
			return null;
		}
		return new CacheRegionImpl<>(regionName, buildCache,
				CacheProvider.fromName(config.getProvider()));
	}

	@Override
	public synchronized Cache<String, Object> getCache(String regionName) {
		if (!cacheEnabled) {
			return dummyRegion;
		}

		CacheRegion<String, Object> cacheRegion = regions.get(regionName);
		if (cacheRegion != null) {
			return cacheRegion;
		}

		cacheRegion = buildCacheRegion(regionName);
		regions.put(regionName, cacheRegion);
		return cacheRegion;
	}

	@Override
	public ConcurrentMap<String, Cache<String, Object>> getAllCaches() {
		ConcurrentMap<String, Cache<String, Object>> ret = new ConcurrentHashMap<String, Cache<String, Object>>();
		ret.putAll(regions);
		return ret;
	}

	@Override
	public CacheStats getCacheStats(String regionName) {
		CacheRegion<String, Object> cache = regions.get(regionName);
		if (cache == null) {
			return null;
		}
		return cache.getStats();
	}

	@Override
	public ConcurrentMap<String, CacheStats> getAllCacheStats() {
		ConcurrentMap<String, CacheStats> stats = new ConcurrentHashMap<String, CacheStats>();
		for (String regionName : regions.keySet()) {
			CacheRegion<String, Object> cache = regions.get(regionName);
			if (cache == null) {
				continue;
			}
			stats.put(regionName, cache.getStats());
		}
		return stats;
	}

	@Override
	public void invalidate(String regionName) {
		CacheRegion<String, Object> cache = regions.get(regionName);
		if (cache == null) {
			return;
		}
		cache.invalidateAll();
	}

	@Override
	public void invalidateAll() {
		for (String regionName : regions.keySet()) {
			CacheRegion<String, Object> cache = regions.get(regionName);
			if (cache == null) {
				continue;
			}
			cache.invalidateAll();
		}
	}
}

