package com.catorv.test.gallop.httpclient;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.httpclient.HttpClientModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * HttpClient Test
 * Created by cator on 8/14/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		ConfigurationModule.class,
		LifecycleModule.class,
		HttpClientModule.class
})
public class HttpClientTest {

	@Inject
	private CloseableHttpClient httpClient;

	@Test
	public void test() {
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		try {
			try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
				String string = EntityUtils.toString(response.getEntity(),
						Charset.defaultCharset());
				Assert.assertTrue(string.length() > 0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
