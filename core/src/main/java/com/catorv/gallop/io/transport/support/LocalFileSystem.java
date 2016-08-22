package com.catorv.gallop.io.transport.support;


import com.catorv.gallop.io.transport.*;
import com.catorv.gallop.util.StreamUtils;

import java.io.*;

public class LocalFileSystem implements FileSystem {

	private String partition;

	public LocalFileSystem() {
		this(".");
	}

	public LocalFileSystem(String path) {
		String absolutePath = this.resolve(path);
		absolutePath = absolutePath.replace(File.separator, "/");
		if (absolutePath.startsWith("/")) {
			return;
		}
		int index = absolutePath.indexOf('/', 0);
		this.partition = absolutePath.substring(0, index);
	}

	@Override
	public String getPartition() {
		return partition;
	}

	@Override
	public String getScheme() {
		return "watch";
	}

	@Override
	public String getHost() {
		return null;
	}

	@Override
	public String[] getChild(String path) throws IOException {
		File file = new File(path);
		return file.list();
	}

	@Override
	public String resolve(String path) {
		String absolutePath = (new File(path)).getAbsolutePath();
		absolutePath = absolutePath.replace(File.separator, "/");
		if (!absolutePath.startsWith("/")) {
			absolutePath = "/" + absolutePath;
		}
		return absolutePath;
	}

	@Override
	public void write(String path, InputStream is) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			StreamUtils.copyStream(is, fos);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	@Override
	public void read(String path, OutputStream os) throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			StreamUtils.copyStream(fis, os);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	@Override
	public boolean exists(String path) throws IOException {
		return (new File(path)).exists();
	}

	@Override
	public Path mkdir(String parent, String name) throws IOException {
		return mkdirs(new File(parent, name));
	}

	@Override
	public Path mkdirs(String parent, String path) throws IOException {
		return mkdirs(new File(parent + "/" + path));
	}

	private Path mkdirs(File file) {
		if (!file.exists()) {
			file.mkdirs();
		}
		return new LocalFilePath(file.getAbsolutePath());
	}

	@Override
	public boolean isFile(String path) throws IOException {
		return (new File(path)).isFile();
	}

	@Override
	public boolean isDirectory(String path) throws IOException {
		return (new File(path)).isDirectory();
	}

	@Override
	public void remove(String path) throws IOException {
		(new File(path)).delete();
	}
}
