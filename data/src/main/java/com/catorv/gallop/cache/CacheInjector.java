package com.catorv.gallop.cache;

import com.google.inject.MembersInjector;

import java.lang.reflect.Field;

/**
 * Cache Injector
 * Created by cator on 8/4/16.
 */
public class CacheInjector<T> implements MembersInjector<T> {

	private Field field;
	private Cache<String, Object> cache;

	public CacheInjector(Field field, String regionName,
	                     CacheManager<String, Object> cacheManager) {
		this.field = field;
		cache = cacheManager.getCache(regionName);
		field.setAccessible(true);
	}

	@Override
	public void injectMembers(T ins) {
		try {
			field.set(ins, cache);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
