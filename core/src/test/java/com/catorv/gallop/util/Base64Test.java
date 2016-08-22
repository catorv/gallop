package com.catorv.gallop.util;

import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.common.base.Charsets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
public class Base64Test {

	public String string = "cator";

	@Test
	public void testString() {
		Assert.assertEquals(Base64.decode(Base64.encode(string)), string);
	}

	@Test
	public void testBytes() {
		byte[] bytes = string.getBytes(Charsets.UTF_8);
		byte[] result = Base64.decode(Base64.encode(bytes));
		Assert.assertEquals(new String(result), string);
	}

}
