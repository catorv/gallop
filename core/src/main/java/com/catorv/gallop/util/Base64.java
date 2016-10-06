package com.catorv.gallop.util;

/**
 * Base64编码和解码算法
 * Created by cator on 8/2/16.
 */
public class Base64 {

	public static byte[] encode(byte[] binaryData) {
		return org.apache.commons.codec.binary.Base64.encodeBase64(binaryData);
	}

	public static String encode(String string) {
		return org.apache.commons.codec.binary.Base64.encodeBase64String(
				string.getBytes());
	}

	public static byte[] decode(byte[] base64Data) {
		return org.apache.commons.codec.binary.Base64.decodeBase64(base64Data);
	}

	public static String decode(String base64String) {
		return new String(org.apache.commons.codec.binary.Base64.decodeBase64(
				base64String));
	}

}
