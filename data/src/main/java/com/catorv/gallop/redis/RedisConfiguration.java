package com.catorv.gallop.redis;

import com.google.inject.name.Named;

/**
 * Redis Configuration
 * Created by cator on 8/2/16.
 */
public class RedisConfiguration {

	/** Redis服务器主机名或IP地址 */
	private String host;

	/** Redis服务器端口号 */
	private int port;

	/** 连接超时时间 */
	private int timeout;

	/** 连接密码 */
	private String password;

	/** 数据库序号 */
	private int database;

	/** 客户端名称 */
	private String clientName;

	/** 连接池最大连接数 */
	@Named("pool.maxTotal")
	private int maxTotal;

	/** 连接池空闲最小连接数 */
	@Named("pool.minIdle")
	private int minIdle;

	/** 连接池空闲最大连接数 */
	@Named("pool.maxIdle")
	private int maxIdle;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
}
