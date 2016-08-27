package com.catorv.test.gallop.util;

import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.catorv.gallop.util.CRC;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The testing for CRC.
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
public class CRCTest {

	private String string = "cator";

	@Test
	public void testCRC16() {
		Assert.assertEquals(CRC.crc16(string), 34925);
	}

	@Test
	public void testCRC32() {
		Assert.assertEquals(CRC.crc32(string), 2495401234L);
	}

}
