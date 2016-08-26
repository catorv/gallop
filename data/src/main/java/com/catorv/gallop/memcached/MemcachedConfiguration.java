package com.catorv.gallop.memcached;

/**
 * Memcached配置项
 * Created by cator on 8/2/16.
 */
public class MemcachedConfiguration {

	/** Memcached服务器地址,格式: hostname/ip:port,多个地址用空格分隔 */
	private String addresses;

	/** 服务器权重,多个权重用空格分隔,缺省的权重为1 */
	private String weights;

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public void setWeights(String weights) {
		this.weights = weights;
	}

	public String getWeights() {
		return weights;
	}
}

