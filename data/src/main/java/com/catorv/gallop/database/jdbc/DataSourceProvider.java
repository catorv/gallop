package com.catorv.gallop.database.jdbc;

import com.catorv.gallop.cfg.ConfigurationBuilder;
import com.catorv.gallop.lifecycle.Destroy;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Singleton
public class DataSourceProvider implements Closeable, Provider<DataSource> {

	private Map<String, DataSource> dataSources = new HashMap<>();

	@Inject
	private ConfigurationBuilder configBuilder;

	public synchronized DataSource getDataSource(String jndi) {
		if (Strings.isNullOrEmpty(jndi)) {
			jndi = "jdbc";
		}
		DataSource dataSource = dataSources.get(jndi);
		if (dataSource != null) {
			return dataSource;
		}
		try {
			dataSource = buildDataSource(jndi);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Preconditions.checkNotNull(dataSource);
		dataSources.put(jndi, dataSource);
		return dataSource;
	}

	private BasicDataSource buildDataSource(String jndi) throws SQLException {
		DataSourceConfiguration config = configBuilder.build(jndi.replace("/", "."),
				DataSourceConfiguration.class);

		BasicDataSource ds = new BasicDataSource();

		String driver = config.getDriver();
		if (Strings.isNullOrEmpty(driver)) {
			ds.setDriverClassName(driver);
		}

		ds.setUrl(config.getConnectionUrl());
		ds.setUsername(config.getUsername());
		ds.setPassword(config.getPassword());

		int maxSize = Math.max(1, config.getMaxActive());
		ds.setInitialSize(Math.min(maxSize, config.getInitialSize()));
		ds.setMaxTotal(maxSize);
		ds.setMinIdle(Math.min(maxSize, Math.max(0, config.getMinIdle())));

		ds.setRemoveAbandonedOnBorrow(config.isRemoveAbandonedOnBorrow());
		ds.setRemoveAbandonedTimeout(config.getRemoveAbandonedTimeout());
		ds.setRemoveAbandonedOnMaintenance(config.isRemoveAbandonedOnMaintenance());
		ds.setLogAbandoned(config.isLogAbandoned());

		ds.setTimeBetweenEvictionRunsMillis(config.getTimeBetweenEvictionRunsMillis());
		ds.setMinEvictableIdleTimeMillis(config.getMinEvictableIdleTimeMillis());

		ds.setTestOnBorrow(config.isTestOnBorrow());
		ds.setTestOnReturn(config.isTestOnReturn());
		ds.setTestWhileIdle(config.isTestWhileIdle());

		ds.setDefaultAutoCommit(config.isDefaultAutoCommit());
		ds.setDefaultReadOnly(config.isDefaultReadOnly());

		ds.setValidationQuery(config.getValidationQuery());

		List<String> sqls = new ArrayList<>();
		if (Strings.isNullOrEmpty(config.getInitSqls())) {
			sqls.add("set names utf8mb4");
		} else if (config.getInitSqls().contains(";")) {
			sqls.addAll(Arrays.asList(config.getInitSqls().split(";")));
		} else {
			sqls.add(config.getInitSqls());
		}
		ds.setConnectionInitSqls(sqls);

		return ds;
	}

	@Override
	@Destroy
	public synchronized void close() throws IOException {
		for (DataSource ds : dataSources.values()) {
			try {
				((BasicDataSource) ds).close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public DataSource get() {
		return getDataSource(null);
	}

}