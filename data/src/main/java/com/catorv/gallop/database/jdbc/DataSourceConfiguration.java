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

	private Integer initialSize = 0;
	private Integer maxActive = 8;
	private Integer minIdle = 0;
	private Long maxWait = 30 * 1000L;

	private Boolean removeAbandoned = false;
	private Integer removeAbandonedTimeout = 300;
	private Boolean logAbandoned = false;

	private Long timeBetweenEvictionRunsMillis = 60 * 1000L;
	private Long minEvictableIdleTimeMillis = 30 * 60 * 1000L;

	private Boolean testOnBorrow = false;
	private Boolean testOnReturn = false;
	private Boolean testWhileIdle = true;

	private Boolean defaultAutoCommit = true;
	private Boolean defaultReadOnly = false;
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

	public Integer getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public Integer getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(Long maxWait) {
		this.maxWait = maxWait;
	}

	public Boolean getRemoveAbandoned() {
		return removeAbandoned;
	}

	public void setRemoveAbandoned(Boolean removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}

	public Integer getRemoveAbandonedTimeout() {
		return removeAbandonedTimeout;
	}

	public void setRemoveAbandonedTimeout(Integer removeAbandonedTimeout) {
		this.removeAbandonedTimeout = removeAbandonedTimeout;
	}

	public Boolean getLogAbandoned() {
		return logAbandoned;
	}

	public void setLogAbandoned(Boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
	}

	public Long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public Long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public Boolean getTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(Boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public Boolean getDefaultAutoCommit() {
		return defaultAutoCommit;
	}

	public void setDefaultAutoCommit(Boolean defaultAutoCommit) {
		this.defaultAutoCommit = defaultAutoCommit;
	}

	public Boolean getDefaultReadOnly() {
		return defaultReadOnly;
	}

	public void setDefaultReadOnly(Boolean defaultReadOnly) {
		this.defaultReadOnly = defaultReadOnly;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}
}
