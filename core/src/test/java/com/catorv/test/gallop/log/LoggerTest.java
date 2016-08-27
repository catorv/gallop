package com.catorv.test.gallop.log;

import com.catorv.gallop.log.InjectLogger;
import com.catorv.gallop.log.LoggerFactory;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

/**
 * Logger Test
 * Created by cator on 8/1/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		LoggerModule.class
})
public class LoggerTest {

	@InjectLogger
	private Logger logger1;

	private Logger logger2;

	public LoggerTest() {

	}

	@Inject
	protected LoggerTest(LoggerFactory loggerFactory) {
		logger2 = loggerFactory.getLogger(LoggerTest.class);
	}

	@Test
	public void testLog() {
		logger1.info("perform log");
		logger2.info("perform log");
		Assert.assertSame(logger1, logger2);
	}
}

