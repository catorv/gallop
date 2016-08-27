package com.catorv.test.gallop.job.schedule;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.job.JobModule;
import com.catorv.gallop.job.schedule.ScheduleManager;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.lifecycle.MethodInvocation;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;

import java.lang.reflect.Method;
import java.text.ParseException;

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
public class ScheduleManagerTest {

	@Inject
	private ScheduleManager scheduleManager;

	private int i = 0;

	@Test
	public void test() throws ParseException, NoSuchMethodException, SchedulerException, InterruptedException {
		Method method = this.getClass().getDeclaredMethod("job");
		MethodInvocation methodInvocation = new MethodInvocation(method, this);
		scheduleManager.schedule("0/1 * * * * ? *", methodInvocation);
		Thread.sleep(5000);

		System.out.println(i);
		Assert.assertEquals(2, i);
	}

	public void job() throws InterruptedException {
		Thread.sleep(2000);
		i++;
	}

}
