package com.catorv.gallop.cache;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.reflect.Field;

/**
 * Cache type listener
 * Created by cator on 8/4/16.
 */
public class CacheTypeListener implements TypeListener {

	@Inject
	private CacheManager<String, Object> cacheManager;

	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		Class<?> clazz = type.getRawType();
		while (clazz != null && clazz != Object.class) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getType() == Cache.class && field.isAnnotationPresent(Cacheable.class)) {
					Cacheable cacheable = field.getAnnotation(Cacheable.class);
					encounter.register(new CacheInjector<I>(field, cacheable.region(), cacheManager));
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

}
