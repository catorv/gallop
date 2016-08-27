package com.catorv.test.gallop.job;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.job.Async;
import com.catorv.gallop.job.AsyncResult;
import com.catorv.gallop.job.JobModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * Created by cator on 8/11/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		ConfigurationModule.class,
		LifecycleModule.class,
		LoggerModule.class,
		JobModule.class
})
public class AsyncTest {

	private boolean job1Done = false;

	private boolean job2Done = false;

	private boolean job3Done = false;

	@Test
	public void test() throws InterruptedException, ExecutionException {
		job1();
		Future<String> result = job2();
		String result2 = job3();

		Assert.assertEquals("OK3", result2);

		Assert.assertFalse(job1Done);
		Assert.assertFalse(job2Done);
		Assert.assertTrue(job3Done);


		Assert.assertEquals("OK", result.get());
		Assert.assertFalse(job1Done);
		Assert.assertTrue(job2Done);

		Thread.sleep(1200);

		Assert.assertTrue(job1Done);
	}

	@Async
	public void job1() throws InterruptedException {
		Thread.sleep(3000);
		System.out.println("## JOB1");
		job1Done = true;
	}

	@Async
	public Future<String> job2() throws InterruptedException {
		Thread.sleep(2000);
		System.out.println("## JOB2");
		job2Done = true;
		return AsyncResult.of("OK");
	}

	@Async
	public String job3() throws InterruptedException {
		Thread.sleep(1000);
		System.out.println("## JOB3");
		job3Done = true;
		return "OK3";
	}

}
