package com.catorv.gallop.database.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据库时间戳工具类
 * Created by cator on 8/2/16.
 */
public class TimestampUtils {

	public static int toInt(Timestamp timestamp) {
		if (timestamp == null) {
			return 0;
		}

		return (int) (timestamp.getTime() / 1000);
	}

	public static Date toDate(Timestamp timestamp) {
		if (timestamp == null) {
			return new Date();
		}

		return new Date(timestamp.getTime());
	}

	public static Timestamp fromInt(int seconds) {
		return fromTime(seconds * 1000L);
	}

	public static Timestamp fromTime(long milliseconds) {
		return new Timestamp(milliseconds);
	}

	public static Timestamp fromDate(Date date) {
		return fromTime(date.getTime());
	}

	public static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static String format(Timestamp timestamp, String pattern) {
		if (null == timestamp) {
			return "";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(toDate(timestamp));
	}

}
