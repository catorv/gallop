package com.catorv.gallop.database.jpa;

import com.catorv.gallop.database.jdbc.DataSourceProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

import java.util.Map;
import java.util.Properties;

/**
 * Local DataSource Hibernate Persistence Provider
 * Created by cator on 8/11/16.
 */
public class LocalDSHibernatePersistenceProvider extends HibernatePersistenceProvider {

	private DataSourceProvider dataSourceProvider;

	private Properties properties;

	public LocalDSHibernatePersistenceProvider(DataSourceProvider dataSourceProvider,
	                                           Properties properties) {
		this.dataSourceProvider = dataSourceProvider;
		this.properties = properties;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected EntityManagerFactoryBuilder getEntityManagerFactoryBuilder(
			PersistenceUnitDescriptor persistenceUnitDescriptor,
			Map integration,
			ClassLoader providedClassLoader
	) {
		return super.getEntityManagerFactoryBuilder(
				new PersistenceUnitDescriptorWrapper(persistenceUnitDescriptor,
						dataSourceProvider, properties),
				integration,
				providedClassLoader
		);
	}
}

