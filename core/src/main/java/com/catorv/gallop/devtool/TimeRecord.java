package com.catorv.gallop.devtool;

/**
 * Created by cator on 23/11/2016.
 */
public class TimeRecord {

	private final String name;
	private long start;
	private long end;

	public TimeRecord(String name) {
		this.name = name;
	}

	public void start() {
		this.start = System.currentTimeMillis();
	}

	public void end() {
		this.end = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return name + "=" + (end - start) + "ms";
	}
}
