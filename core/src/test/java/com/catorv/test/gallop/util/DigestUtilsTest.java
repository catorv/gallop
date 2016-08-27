package com.catorv.test.gallop.util;

import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.catorv.gallop.util.DigestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The testing for DigestUtils.
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
public class DigestUtilsTest {

	private String string = "cator";

	@Test
	public void testMD5() {
		Assert.assertEquals(DigestUtils.md5(string), "671711a7ce87226106994d106158b31c");
	}

	@Test
	public void testSHA1() {
		Assert.assertEquals(DigestUtils.sha1(string), "7834b483cfe6bfd8373fa3ff1aa2aeb10d233a18");
	}

	@Test
	public void testSHA256() {
		Assert.assertEquals(DigestUtils.sha256(string), "60e9ab8323f1ef8e84783c49c6146bd11c697a885d7e3ba457c2f456c9424edf");
	}

}
