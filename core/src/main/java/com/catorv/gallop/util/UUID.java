package com.catorv.gallop.util;

/**
 * UUID工具库
 * Created by cator on 8/2/16.
 */
public class UUID {

	/**
	 * 获取UUID(带'-',共36位)
	 * @return UUID字符串
	 */
	public static String getUUID() {
		return java.util.UUID.randomUUID().toString();
	}

	public static String getUUID32() {
		java.util.UUID uuid = java.util.UUID.randomUUID();
		return String.format("%016x%016x", uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
	}

}
