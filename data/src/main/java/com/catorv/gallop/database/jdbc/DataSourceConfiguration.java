package com.catorv.gallop.database.jdbc;

import com.google.inject.name.Named;

/**
 * Data Source Configuration
 * Created by cator on 8/11/16.
 */
public class DataSourceConfiguration {

	@Named("url")
	private String connectionUrl;
	private String username;
	private String password;
	private String driver;
	private String initSqls;

	private int initialSize = 20;
	private int maxActive = 50;
	private int minIdle = 5;

	private boolean removeAbandonedOnBorrow = true;
	private boolean removeAbandonedOnMaintenance = true;
	private int removeAbandonedTimeout = 1800;
	private boolean logAbandoned = true;

	private long timeBetweenEvictionRunsMillis = 60 * 1000L;
	private long minEvictableIdleTimeMillis = 30 * 60 * 1000L;

	private boolean testOnBorrow = true;
	private boolean testOnReturn = true;
	private boolean testWhileIdle = true;

	private boolean defaultAutoCommit = true;
	private boolean defaultReadOnly = false;
	private String validationQuery = "select 1";

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getInitSqls() {
		return initSqls;
	}

	public void setInitSqls(String initSqls) {
		this.initSqls = initSqls;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public boolean isRemoveAbandonedOnBorrow() {
		return removeAbandonedOnBorrow;
	}

	public void setRemoveAbandonedOnBorrow(boolean removeAbandonedOnBorrow) {
		this.removeAbandonedOnBorrow = removeAbandonedOnBorrow;
	}

	public boolean isRemoveAbandonedOnMaintenance() {
		return removeAbandonedOnMaintenance;
	}

	public void setRemoveAbandonedOnMaintenance(boolean removeAbandonedOnMaintenance) {
		this.removeAbandonedOnMaintenance = removeAbandonedOnMaintenance;
	}

	public int getRemoveAbandonedTimeout() {
		return removeAbandonedTimeout;
	}

	public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
		this.removeAbandonedTimeout = removeAbandonedTimeout;
	}

	public boolean isLogAbandoned() {
		return logAbandoned;
	}

	public void setLogAbandoned(boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public boolean isDefaultAutoCommit() {
		return defaultAutoCommit;
	}

	public void setDefaultAutoCommit(boolean defaultAutoCommit) {
		this.defaultAutoCommit = defaultAutoCommit;
	}

	public boolean isDefaultReadOnly() {
		return defaultReadOnly;
	}

	public void setDefaultReadOnly(boolean defaultReadOnly) {
		this.defaultReadOnly = defaultReadOnly;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}
}
