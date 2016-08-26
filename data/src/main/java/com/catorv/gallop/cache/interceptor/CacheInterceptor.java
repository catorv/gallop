package com.catorv.gallop.cache.interceptor;

import com.catorv.gallop.cache.Cache;
import com.catorv.gallop.cache.CacheManager;
import com.catorv.gallop.cache.Cacheable;
import com.catorv.gallop.cache.key.CacheKeyGenerator;
import com.catorv.gallop.log.InjectLogger;
import com.google.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * the interceptor of a cacheable method
 * Created by cator on 8/3/16.
 */
public class CacheInterceptor implements MethodInterceptor {

	@Inject
	private CacheManager<String, Object> cacheManager;

	@InjectLogger
	private Logger logger;

	private final static NullValueObject NULL_OBJ = new NullValueObject();

	private ConcurrentMap<String, CacheKeyGenerator> keyGenerators = new ConcurrentHashMap<>();

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		Method method = mi.getMethod();
		logger.debug("perform cache on method {}", method);

		Cacheable cacheable = method.getAnnotation(Cacheable.class);
		if (cacheable == null) {
			cacheable = method.getDeclaringClass().getAnnotation(Cacheable.class);
		}
		String region = cacheable.region();

		Class<? extends CacheKeyGenerator> keyGeneratorClass = cacheable.keyGenerator();
		CacheKeyGenerator generator = keyGenerators.get(keyGeneratorClass.toString());
		if (generator == null) {
			generator = keyGeneratorClass.newInstance();
			keyGenerators.put(keyGeneratorClass.toString(), generator);
		}
		String cacheKey = generator.getKey(method, mi.getArguments());

		Cache<String, Object> cache = cacheManager.getCache(region);
		Object proceed = cache.get(cacheKey);

		if (proceed != null) {
			logger.debug("Hit cache key {} from region {}", cacheKey, region);
			return NULL_OBJ.equals(proceed) ? null : proceed;
		}

		proceed = mi.proceed();
		if (proceed == null) {
			proceed = NULL_OBJ;
		}
		cache.put(cacheKey, proceed);

		return NULL_OBJ.equals(proceed) ? null : proceed;
	}
}

