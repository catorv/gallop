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
public class UUIDTest {
	@Test
	public void test() {
		String uuid = UUID.getUUID();
		String uuid32 = UUID.getUUID32();

		Assert.assertEquals(uuid.length(), 36);
		Assert.assertEquals(uuid32.length(), 32);
	}
}
