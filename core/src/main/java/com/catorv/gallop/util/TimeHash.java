package com.catorv.gallop.util;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by cator on 25/10/2016.
 */
public class TimeHash {

	public static String get(long millis, long interval) {
		return Long.toHexString(millis / interval);
	}

	public static String hourly(long millis) {
		return get(millis, 60 * 60 * 1000);
	}

	public static String daily(long millis) {
		return get(millis, 24 * 60 * 60 * 1000);
	}

	public static String monthly(long millis) {
		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));
		calendar.setTimeInMillis(millis);
		final int month = calendar.get(Calendar.MONTH);
		final int year = calendar.get(Calendar.YEAR);
		return Integer.toHexString(year * 12 + month);
	}

	public static String yearly(long millis) {
		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));
		calendar.setTimeInMillis(millis);
		final int year = calendar.get(Calendar.YEAR);
		return Integer.toHexString(year);
	}

}
