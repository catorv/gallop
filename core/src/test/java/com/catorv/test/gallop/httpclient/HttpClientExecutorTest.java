package com.catorv.test.gallop.httpclient;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.httpclient.HttpClientExecutor;
import com.catorv.gallop.httpclient.HttpClientModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * HttpClientExecutor Test
 * Created by cator on 8/15/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		ConfigurationModule.class,
		LifecycleModule.class,
		HttpClientModule.class
})
public class HttpClientExecutorTest {

	@Inject
	private HttpClientExecutor executor;

	@Test
	public void test() throws URISyntaxException, IOException {
		String string = executor.get("http://www.baidu.com")
				.parameter("wd", "haha")
				.execute()
				.returnString();

		Assert.assertTrue(string.length() > 0);
	}

}
