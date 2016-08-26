package com.catorv.gallop.cache;

import com.catorv.gallop.cache.adapter.CacheBuilder;
import com.catorv.gallop.cache.adapter.CacheBuilderHub;
import com.catorv.gallop.cache.interceptor.CacheInterceptor;
import com.catorv.gallop.inject.AbstractNamedModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

import java.lang.reflect.AnnotatedElement;

/**
 * Cache Module
 * 依赖模块:
 *  ConfigurationModule
 *  LoggerModule
 *  JsonModule
 *  MemcachedModule
 *  RedisModule
 * Created by cator on 8/3/16.
 */
public class CacheModule extends AbstractNamedModule {

	public CacheModule() {
		this(null);
	}

	public CacheModule(String name) {
		super(name);
	}

	@Override
	protected void configure() {
		TypeLiteral<CacheBuilder<String, Object>> builderTypeLiteral;
		builderTypeLiteral = new TypeLiteral<CacheBuilder<String, Object>>() {};
		TypeLiteral<CacheManager<String, Object>> managerTypeLiteral;
		managerTypeLiteral = new TypeLiteral<CacheManager<String, Object>>() {};

		if (name != null) {
			bindNamespaceAnnotated(managerTypeLiteral).toProvider(CacheManagerProvider.class);
		}
		bind(builderTypeLiteral).to(CacheBuilderHub.class);
		bind(managerTypeLiteral).toProvider(CacheManagerProvider.class);

		CacheInterceptor cacheInterceptor = new CacheInterceptor();
		requestInjection(cacheInterceptor);

		Matcher<AnnotatedElement> matcher = Matchers.annotatedWith(Cacheable.class);
		bindInterceptor(matcher, Matchers.any(), cacheInterceptor);
		bindInterceptor(Matchers.any(), matcher, cacheInterceptor);

		CacheTypeListener listener = new CacheTypeListener();
		requestInjection(listener);
		bindListener(Matchers.any(), listener);
	}
}

