package com.catorv.gallop.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {

	private static final String JOB_PREFIX = "J";
	private static final String TASK_PREFIX = "T";
	private static final String TRIGGER_PREFIX = "I";

	private static final AtomicInteger JOB_COUNTER = new AtomicInteger(0);
	private static final AtomicInteger TASK_COUNTER = new AtomicInteger(0);
	private static final AtomicInteger TRIGGER_COUNTER = new AtomicInteger(0);

	public static String nextJobID() {
		return JOB_PREFIX + nextID(JOB_COUNTER);
	}

	public static String nextTaskID() {
		return TASK_PREFIX + nextID(TASK_COUNTER);
	}

	public static String nextTriggerID() {
		return TRIGGER_PREFIX + nextID(TRIGGER_COUNTER);
	}

	private static String nextID(AtomicInteger counter) {
		String date = (new SimpleDateFormat("yyMMddHHmmssSSS")).format(new Date());
		int index = Math.abs(counter.addAndGet(1)) % 1000;
		return String.format("%s%03d", date, index);
	}

}
