package com.catorv.gallop.util;

import com.catorv.gallop.test.junit.GuiceTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
public class TemplateTest {
	@Test
	public void test() {
		String template = "Create by {author} on {date}.";
		String result = Template.of(template).evaluate("author", "cator");

		Assert.assertEquals(result, "Create by cator on {date}.");
	}
}
