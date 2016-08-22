package com.catorv.gallop.util;

import java.io.*;

/**
 * Stream处理工具类
 * Created by cator on 8/2/16.
 */
public class StreamUtils {

	public static byte[] readStream(InputStream ins) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copyStream(ins, baos);
		return baos.toByteArray();
	}

	public static void copyStream(InputStream ins, OutputStream os)
			throws IOException {
		byte[] buf = new byte[4096];
		int len = 0;
		while ((len = ins.read(buf)) != -1) {
			os.write(buf, 0, len);
		}
	}

	public static void writeToFile(InputStream ins, File file)
			throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			copyStream(ins, fos);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	public static void readFromFile(File file, OutputStream os)
			throws IOException {
		FileInputStream ins = null;
		try {
			ins = new FileInputStream(file);
			copyStream(ins, os);
		} finally {
			if (ins != null) {
				ins.close();
			}
		}
	}

	public static void closeStream(InputStream ins) {
		try {
			if (ins != null) {
				ins.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void closeStream(OutputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

