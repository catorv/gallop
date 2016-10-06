package com.catorv.gallop.util;

import com.google.common.base.Joiner;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash算法集合
 * Created by cator on 8/1/16.
 */
public class DigestUtils {

	public static String hash(byte[] data) {
		return md5(md5(data) + sha1(data));
	}

	public static String hash(String data) {
		return md5(md5(data) + sha1(data));
	}

	public static String hash(String... strings) {
		return hash(Joiner.on("").join(strings));
	}

	public static String md5(byte[] data) {
		try {
			byte[] digestData = MessageDigest.getInstance("MD5").digest(data);
			return new String(Hex.encodeHex(digestData));
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static String md5(String data) {
		return md5(data.getBytes());
	}

	public static String sha1(byte[] data) {
		try {
			byte[] digestData = MessageDigest.getInstance("SHA-1").digest(data);
			return new String(Hex.encodeHex(digestData));
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static String sha1(String data) {
		return sha1(data.getBytes());
	}

	public static String sha256(byte[] data) {
		try {
			byte[] digestData = MessageDigest.getInstance("SHA-256").digest(data);
			return new String(Hex.encodeHex(digestData));
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static String sha256(String data) {
		return sha256(data.getBytes());
	}


}
