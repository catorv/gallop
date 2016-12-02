package com.catorv.gallop.devtool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cator on 23/11/2016.
 */
public class TimeRecorder {

	private static final Map<String, TimeRecord> records = new HashMap<>();
	private static long start;
	private static long end;

	public static void reset() {
		start = 0;
		end = 0;
		records.clear();
	}

	public static void start() {
		start = System.currentTimeMillis();
	}

	public static void end() {
		end = System.currentTimeMillis();
	}

	public static void start(String name) {
		TimeRecord record = records.get(name);
		if (record == null) {
			record = new TimeRecord(name);
			records.put(name, record);
		}
		record.start();
		if (start == 0) start();
	}

	public static void end(String name) {
		TimeRecord record = records.get(name);
		if (record == null) {
			record = new TimeRecord(name);
			record.start();
			records.put(name, record);
		}
		record.end();
	}

	public static void print() {
		long _end = end == 0 ? System.currentTimeMillis() : end;
		StringBuilder sb = new StringBuilder();
		sb.append("time: ").append(_end - start).append("ms {");
		for (TimeRecord record : records.values()) {
			sb.append(' ').append(record.toString());
		}
		System.out.println(sb.append(" }").toString());
	}

}
