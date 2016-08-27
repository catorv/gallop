package com.catorv.test.gallop.io;

import com.catorv.gallop.io.HashFileOutputStream;
import com.catorv.gallop.util.StreamUtils;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
public class HashFileTest {
	@Test
	public void test() throws IOException, URISyntaxException {
		File file = new File("/Users/cator/Pictures/cator20061210.jpg");
		HashFileOutputStream os = new HashFileOutputStream("/Users/cator/tmp/test/", "jpg");
		FileInputStream ins = new FileInputStream(file);
		StreamUtils.copyStream(ins, os);
		ins.close();
		os.close();
		System.out.println(os.getFile().getAbsolutePath());
		System.out.println(os.getRelativePath());
	}
}
