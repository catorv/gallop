package com.catorv.test.gallop.util;

import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.catorv.gallop.util.Shell;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
public class ShellTest {
	@Test
	public void test() throws IOException, InterruptedException {
		OutputStream os = new ByteArrayOutputStream();
		int returnValue = Shell.command("ls").args("-l")
				.directory(new File("/")).timeout(3000L).execute(os);

		System.out.println(os.toString());

		Assert.assertEquals(0, returnValue);
		Assert.assertFalse(os.toString().isEmpty());
	}
}
