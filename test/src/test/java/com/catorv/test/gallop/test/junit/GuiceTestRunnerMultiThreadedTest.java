package com.catorv.test.gallop.test.junit;

import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.catorv.gallop.test.junit.MultiThreaded;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 含外部模块的测试用例
 * Created by cator on 6/20/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule(SampleModule.class)
@MultiThreaded(numThreads = 10)
public class GuiceTestRunnerMultiThreadedTest extends AbstractModule {

	@Inject
	@Named("field")
	private String testField;

	@Inject
	@Named("field2")
	private String testField2;

	@Inject
	@Named("num")
	private String testFieldNum;

	private static AtomicInteger num = new AtomicInteger(0);

	@Override
	protected void configure() {
		Map<String, String> properties = new HashMap<>();
		properties.put("field", "abc");
		properties.put("num", String.valueOf(num.incrementAndGet()));
		Names.bindProperties(this.binder(), properties);
	}

	@Test
	public synchronized void testApp() throws InterruptedException {
		Assert.assertEquals("abc", testField);
		Assert.assertEquals("xyz", testField2);
		long millis = Math.abs(new Random().nextLong()) % 3000;
		Thread.sleep(millis);
		System.out.println(testFieldNum + " OK in " + millis + "ms in " + Thread.currentThread());
	}
}
