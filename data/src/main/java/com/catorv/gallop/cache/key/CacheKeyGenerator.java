package com.catorv.gallop.cache.key;

import java.lang.reflect.Method;

/**
 * Cache Key Generator
 * Created by cator on 8/3/16.
 */
public interface CacheKeyGenerator {
	public String getKey(Method method, Object... objs);
}

