package com.catorv.gallop.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 安全的类型转工具类
 * Created by cator on 8/3/16.
 */
public class TypeCast {

	/**
	 * 字符串转换成double
	 * @param string 字符串
	 * @param defaultValue 默认值
	 * @return 如果转换成功,返回 double 型数值,否则返回 defaultValue
	 */
	public static double doubleOf(String string, double defaultValue) {
		try {
			return Double.parseDouble(string);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 字符串转换成double
	 * @param string 字符串
	 * @return 如果转换成功,返回 double 型数值,否则返回 0.0
	 */
	public static double doubleOf(String string) {
		return doubleOf(string, 0.0);
	}

	/**
	 * 字符串转换成float型
	 * @param string 字符
	 * @param defaultValue 默认值
	 * @return 如果转换成功,返回float型字符,否则返回 defaultValue
	 */
	public static float floatOf(String string, float defaultValue) {
		try {
			return Float.parseFloat(string);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 字符串转换成float型
	 * @param string 字符
	 * @return 如果转换成功,返回float型字符,否则返回 0.0
	 */
	public static float floatOf(String string) {
		return floatOf(string, (float) 0.0);
	}

	/**
	 * 字符串转换成long型
	 * @param string 字符串
	 * @param defaultValue 默认值
	 * @return 如果转换成功,返回long型数值,否则返回 defaultValue
	 */
	public static long longOf(String string, long defaultValue) {
		try {
			return ((Double) Double.parseDouble(string)).longValue();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 字符串转换成long型
	 * @param string 字符串
	 * @return 如果转换成功,返回long型数值,否则返回 0L
	 */
	public static long longOf(String string) {
		return longOf(string, 0L);
	}

	/**
	 * 字符串转换成int型
	 * @param string 字符串
	 * @param defaultValue 默认值
	 * @return 如果转换成功,返回int型数值,否则返回 defaultValue
	 */
	public static int intOf(String string, int defaultValue) {
		try {
			return ((Double) Double.parseDouble(string)).intValue();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 字符串转换成int型
	 * @param string 字符串
	 * @return 如果转换成功,返回int型数值,否则返回 0
	 */
	public static int intOf(String string) {
		return intOf(string, 0);
	}

	/**
	 * 字符串转换成逻辑型
	 * @param string 字符串
	 * @return 如果字符串值为"true"或"yes",则返回true,否则返回false
	 */
	public static boolean booleanOf(String string) {
		if (string == null) {
			return false;
		}
		final String lower = string.toLowerCase();
		return lower.equals("true") || lower.equals("yes");
	}

	/**
	 * 字符串转换成Date型
	 * @param string 字符串
	 * @param pattern 日期格式模板(SimpleDateFormat格式)
	 * @return 如果转换成功,返回Date型对象,否则返回null
	 */
	public static Date dateOf(String string, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(string);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 任意对象类型转换成String型
	 * @param object 对象
	 * @return 如果为空,则返回"null",否则返回object.toString()
	 */
	public static String stringOf(Object object) {
		if (object == null) {
			return "null";
		}
		return object.toString();
	}

	public static <T> T objectOf(Class<? extends T> type, Object object) {
		if (object == null || !type.isInstance(object)) {
			return null;
		}
		try {
			return type.cast(object);
		} catch (Exception e) {
			return null;
		}
	}

}
