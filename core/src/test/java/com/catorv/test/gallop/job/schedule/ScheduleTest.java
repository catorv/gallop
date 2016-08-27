package com.catorv.test.gallop.job.schedule;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.job.JobModule;
import com.catorv.gallop.job.schedule.Schedule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Schedule Test
 * Created by cator on 8/10/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		ConfigurationModule.class,
		LifecycleModule.class,
		LoggerModule.class,
		JobModule.class
})
public class ScheduleTest {

	private int i = 0;

	@Test
	public void test() throws InterruptedException {
		Thread.sleep(5000);

		System.out.println(i);
		Assert.assertEquals(2, i);
	}

	@Schedule("0/1 * * * * ? *")
	public void job() throws InterruptedException {
		Thread.sleep(2000);
		i++;
	}

	@Schedule("0/1 * * * * ? *")
	public void job2() throws InterruptedException {
		Thread.sleep(2000);
	}
}
