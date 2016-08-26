package com.catorv.gallop.database.jpa;

import com.catorv.gallop.database.jdbc.DataSourceProvider;
import org.hibernate.bytecode.enhance.spi.EnhancementContext;
import org.hibernate.cfg.Environment;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * Persistence Unit Descriptor Wrapper
 * Created by cator on 8/11/16.
 */
public class PersistenceUnitDescriptorWrapper implements PersistenceUnitDescriptor {

	private PersistenceUnitDescriptor descriptor;

	private DataSourceProvider dataSourceProvider;

	private Properties properties;

	public PersistenceUnitDescriptorWrapper(PersistenceUnitDescriptor descriptor,
	                                        DataSourceProvider dataSourceProvider,
	                                        Properties properties) {
		this.descriptor = descriptor;
		this.dataSourceProvider = dataSourceProvider;
		this.properties = properties;
	}

	@Override
	public ClassLoader getClassLoader() {
		return descriptor.getClassLoader();
	}

	@Override
	public ClassLoader getTempClassLoader() {
		return descriptor.getTempClassLoader();
	}

	@Override
	public void pushClassTransformer(EnhancementContext enhancementContext) {
		descriptor.pushClassTransformer(enhancementContext);
	}

	@Override
	public List<URL> getJarFileUrls() {
		return descriptor.getJarFileUrls();
	}

	@Override
	public Object getJtaDataSource() {
		return getDataSource0();
	}

	public DataSource getDataSource0() {
		Object jndi = descriptor.getNonJtaDataSource();
		if (jndi == null) {
			jndi = descriptor.getJtaDataSource();
		}
		return dataSourceProvider.getDataSource(jndi == null ? null : String.valueOf(jndi));
	}

	@Override
	public List<String> getManagedClassNames() {
		return descriptor.getManagedClassNames();
	}

	@Override
	public List<String> getMappingFileNames() {
		return descriptor.getMappingFileNames();
	}

	@Override
	public String getName() {
		return descriptor.getName();
	}

	@Override
	public Object getNonJtaDataSource() {
		return getDataSource0();
	}

	@Override
	public URL getPersistenceUnitRootUrl() {
		return descriptor.getPersistenceUnitRootUrl();
	}

	@Override
	public Properties getProperties() {
		Properties props = descriptor.getProperties();
		if ("never".equals(props.getProperty("customer.cache.use_second_level_cache"))) {
			return props;
		}
		return overrideSecondLevelCacheCfg(props);
	}

	private Properties overrideSecondLevelCacheCfg(Properties properties) {
		// override second level cache configuration
		Boolean useSLC = Boolean.parseBoolean(this.properties
				.getProperty("hibernate.use_second_level_cache"));
		properties.setProperty(Environment.USE_SECOND_LEVEL_CACHE, String.valueOf(useSLC));
		if (!useSLC) {
			properties.setProperty(Environment.CACHE_REGION_FACTORY, "false");
			properties.setProperty(Environment.USE_QUERY_CACHE, "false");
		}
		return properties;
	}

	@Override
	public String getProviderClassName() {
		return descriptor.getProviderClassName();
	}

	@Override
	public SharedCacheMode getSharedCacheMode() {
		return descriptor.getSharedCacheMode();
	}

	@Override
	public PersistenceUnitTransactionType getTransactionType() {
		return descriptor.getTransactionType();
	}

	@Override
	public ValidationMode getValidationMode() {
		return descriptor.getValidationMode();
	}

	@Override
	public boolean isExcludeUnlistedClasses() {
		return descriptor.isExcludeUnlistedClasses();
	}

	@Override
	public boolean isUseQuotedIdentifiers() {
		return descriptor.isUseQuotedIdentifiers();
	}

}

