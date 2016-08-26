package com.catorv.gallop.cache.key;

import com.catorv.gallop.util.DigestUtils;
import com.catorv.gallop.util.TypeCast;

import java.lang.reflect.Method;

/**
 * Default cache key generator
 * Created by cator on 8/3/16.
 */
public class DefaultCacheKeyGenerator implements CacheKeyGenerator {

	@Override
	public String getKey(Method method, Object... objs) {
		StringBuilder sb = new StringBuilder();

		sb.append(method.getDeclaringClass().getName())
				.append(':')
				.append(method.getName());

		if (objs != null) {
			for (Object obj : objs) {
				sb.append(':').append(TypeCast.stringOf(obj));
			}
		}

		return DigestUtils.md5(sb.toString());
	}
}

