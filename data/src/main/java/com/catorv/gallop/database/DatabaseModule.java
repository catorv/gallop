package com.catorv.gallop.database;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.database.interceptor.*;
import com.catorv.gallop.database.jdbc.DataSourceProvider;
import com.catorv.gallop.database.jpa.HibernatePersistenceProviderResolver;
import com.catorv.gallop.inject.AbstractNamedModule;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.persist.jpa.JpaPersistModule;

import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import javax.sql.DataSource;
import java.util.Properties;


/**
 * Database Module
 * 依赖模块:
 *  ConfigurationModule
 *  LifecycleModule
 * Created by cator on 8/11/16.
 */
public class DatabaseModule extends AbstractNamedModule {

	private String jpaUnit;

	public DatabaseModule() {
		this(null);
	}

	public DatabaseModule(String name) {
		super(name);
		jpaUnit = name == null ? "default" : name;
	}

	@Override
	protected void configure() {
		bind(DataSourceProvider.class);
		bind(DataSource.class).toProvider(DataSourceProvider.class);

		PersistenceProviderResolver ppr = new HibernatePersistenceProviderResolver();
		requestInjection(ppr);
		PersistenceProviderResolverHolder.setPersistenceProviderResolver(ppr);

		// JPA Persist
		Properties properties = ConfigurationModule.getProperties(name);
		install(new JpaPersistModule(jpaUnit).properties(properties));
		this.bind(JpaInitializer.class).asEagerSingleton();

		// Query
		QueryMethodInterceptor interceptor = new QueryMethodInterceptor();
		requestInjection(interceptor);
		Matcher<Class> matcher = Matchers.subclassesOf(EntityDAO.class);
		bindInterceptor(matcher, Matchers.annotatedWith(Select.class), interceptor);
		bindInterceptor(matcher, Matchers.annotatedWith(Count.class), interceptor);
		bindInterceptor(matcher, Matchers.annotatedWith(Delete.class), interceptor);
		bindInterceptor(matcher, Matchers.annotatedWith(Update.class), interceptor);
	}
}
