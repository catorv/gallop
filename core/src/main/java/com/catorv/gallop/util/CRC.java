package com.catorv.gallop.util;

import com.google.common.base.Charsets;

import java.util.zip.CRC32;

/**
 * CRC算法
 * Created by cator on 8/2/16.
 */
public class CRC {

	/**
	 * CRC-CCITT (0xFFFF)
	 * @param data
	 * @return
	 */
	public static int crc16(byte[] data) {
		int crc = 0xFFFF;

		for (byte b : data) {
			crc = ((crc >>> 8) | (crc << 8)) & 0xffff;
			crc ^= (b & 0xff);
			crc ^= ((crc & 0xff) >> 4);
			crc ^= (crc << 12) & 0xffff;
			crc ^= ((crc & 0xFF) << 5) & 0xffff;
		}
		crc &= 0xffff;
		return crc;
	}

	public static int crc16(String data) {
		return crc16(data.getBytes(Charsets.UTF_8));
	}

	public static long crc32(byte[] data) {
		CRC32 crc32 = new CRC32();
		crc32.update(data);
		return crc32.getValue();
	}

	public static long crc32(String data) {
		return crc32(data.getBytes(Charsets.UTF_8));
	}

}
