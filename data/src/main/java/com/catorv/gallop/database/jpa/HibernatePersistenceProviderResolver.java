package com.catorv.gallop.database.jpa;

import com.catorv.gallop.database.jdbc.DataSourceProvider;

import javax.inject.Inject;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Hibernate Persistence Provider Resolver
 * Created by cator on 8/11/16.
 */
public class HibernatePersistenceProviderResolver implements PersistenceProviderResolver {

	@Inject
	private DataSourceProvider dataSourceProvider;

	@Inject
	private Properties properties;

	private List<PersistenceProvider> cachedProviders;

	@Override
	public void clearCachedProviders() {
		cachedProviders = null;
	}

	@Override
	public List<PersistenceProvider> getPersistenceProviders() {
		if (cachedProviders == null) {
			cachedProviders = Arrays.asList(new PersistenceProvider[]{
					new LocalDSHibernatePersistenceProvider(dataSourceProvider, properties)
			});
		}
		return cachedProviders;
	}
}
