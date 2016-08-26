package com.catorv.gallop.cache;

import com.catorv.gallop.cache.key.CacheKeyGenerator;
import com.catorv.gallop.cache.key.DefaultCacheKeyGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cacheable annotation
 * Created by cator on 8/3/16.
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

	public String region();

	public Class<? extends CacheKeyGenerator> keyGenerator() default DefaultCacheKeyGenerator.class;
}

