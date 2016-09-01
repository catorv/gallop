package com.catorv.gallop.util;

import com.google.common.base.Strings;

import java.math.BigInteger;

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
		final java.util.UUID uuid = java.util.UUID.randomUUID();
		return String.format("%016x%016x", uuid.getMostSignificantBits(),
				uuid.getLeastSignificantBits());
	}

	public static String getUUID25() {
		final java.util.UUID uuid = java.util.UUID.randomUUID();

		final long high = uuid.getMostSignificantBits();
		final long low = uuid.getLeastSignificantBits();
		final byte[] bytes = new byte[]{
				(byte) (high >> 56), (byte) (high >> 48),
				(byte) (high >> 40), (byte) (high >> 32),
				(byte) (high >> 24), (byte) (high >> 16),
				(byte) (high >> 8),  (byte) (high),
				(byte) (low >> 56),  (byte) (low >> 48),
				(byte) (low >> 40),  (byte) (low >> 32),
				(byte) (low >> 24),  (byte) (low >> 16),
				(byte) (low >> 8),   (byte) (low)
		};

		return Strings.padStart(new BigInteger(1, bytes).toString(36), 25, '0');
	}

}
