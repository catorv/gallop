package com.catorv.test.gallop.devtool;

import com.catorv.gallop.devtool.TimeRecorder;
import org.junit.Test;

/**
 * Created by cator on 23/11/2016.
 */
public class TimeRecorderTest {

	@Test
	public void test() throws Exception {
		TimeRecorder.start("test");
		Thread.sleep(100);
		TimeRecorder.end("test");

		TimeRecorder.start("test");
		Thread.sleep(100);
		TimeRecorder.end("test");

		TimeRecorder.start("sleep");
		Thread.sleep(100);
		TimeRecorder.end("sleep");

		TimeRecorder.print();
	}
}
