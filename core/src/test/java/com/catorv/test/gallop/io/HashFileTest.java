package com.catorv.test.gallop.io;

import com.catorv.gallop.io.HashFile;
import com.catorv.gallop.io.UdidFile;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.catorv.gallop.util.StreamUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * Created by cator on 8/2/16.
 */
@RunWith(GuiceTestRunner.class)
public class HashFileTest {
	@Test
	public void testHashFile() throws IOException, URISyntaxException {
		File srcFile = new File("/data/test/resources/cator20061210.jpg");
		FileInputStream ins = new FileInputStream(srcFile);

		File destFile = new HashFile("/data/test", "/data/test/resources/cator20061210.jpg");
		FileOutputStream os = new FileOutputStream(destFile);

		StreamUtils.copyStream(ins, os);
		ins.close();
		os.close();

		System.out.println(destFile.getAbsoluteFile());
	}

	@Test
	public void testUdidFile() throws IOException, URISyntaxException {
		File srcFile = new File("/data/test/resources/cator20061210.jpg");
		FileInputStream ins = new FileInputStream(srcFile);

		File destFile = new UdidFile("/data/test", "/data/test/resources/cator20061210.jpg");
		FileOutputStream os = new FileOutputStream(destFile);

		StreamUtils.copyStream(ins, os);
		ins.close();
		os.close();

		System.out.println(destFile.getAbsoluteFile());
	}
}
