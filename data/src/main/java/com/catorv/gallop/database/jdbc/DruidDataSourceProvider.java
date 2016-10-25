package com.catorv.gallop.database.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.catorv.gallop.cfg.ConfigurationBuilder;
import com.catorv.gallop.lifecycle.Destroy;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Singleton
public class DruidDataSourceProvider implements DataSourceProvider {

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
		dataSources.put(jndi, dataSource);
		return dataSource;
	}

	private DruidDataSource buildDataSource(String jndi) throws SQLException {
		DataSourceConfiguration config = configBuilder.build(jndi.replace("/", "."),
				DataSourceConfiguration.class);

		DruidDataSource ds = new DruidDataSource();

		String driver = config.getDriver();
		if (Strings.isNullOrEmpty(driver)) {
			ds.setDriverClassName(driver);
		}

		ds.setUrl(config.getConnectionUrl());
		ds.setUsername(config.getUsername());
		ds.setPassword(config.getPassword());

		int maxSize = Math.max(1, config.getMaxActive());
		ds.setInitialSize(Math.min(maxSize, config.getInitialSize()));
		ds.setMaxActive(maxSize);
		ds.setMinIdle(Math.min(maxSize, Math.max(0, config.getMinIdle())));
		ds.setMaxWait(config.getMaxWait());

		ds.setRemoveAbandoned(config.getRemoveAbandoned());
		ds.setRemoveAbandonedTimeout(config.getRemoveAbandonedTimeout());
		ds.setLogAbandoned(config.getLogAbandoned());

//		ds.setFilters("stat,slf4j");

		ds.setTimeBetweenEvictionRunsMillis(config.getTimeBetweenEvictionRunsMillis());
		ds.setMinEvictableIdleTimeMillis(config.getMinEvictableIdleTimeMillis());

		ds.setTestOnBorrow(config.getTestOnBorrow());
		ds.setTestOnReturn(config.getTestOnReturn());
		ds.setTestWhileIdle(config.getTestWhileIdle());

		ds.setDefaultAutoCommit(config.getDefaultAutoCommit());
		ds.setDefaultReadOnly(config.getDefaultReadOnly());

//		ds.setPoolPreparedStatements(false);
//		ds.setMaxPoolPreparedStatementPerConnectionSize(20);

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
			((DruidDataSource) ds).close();
		}
	}

	@Override
	public DataSource get() {
		return getDataSource(null);
	}
}