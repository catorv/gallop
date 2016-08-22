package com.catorv.gallop.io;

import com.catorv.gallop.io.transport.FileSystems;
import com.catorv.gallop.io.transport.Path;
import com.catorv.gallop.util.UUID;
import com.google.common.base.Strings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;

/**
 * 通过Hash算法,把文件均匀散列存放到多个目录中
 * Created by cator on 8/2/16.
 */
public class HashFileOutputStream extends OutputStream {

	private Path rootPath;
	private File file;
	private FileOutputStream outputStream;

	public HashFileOutputStream(String rootPath) throws IOException, URISyntaxException {
		this(rootPath, null);
	}

	public HashFileOutputStream(String rootPath, String fileExtension)
			throws IOException, URISyntaxException {
		this.rootPath = FileSystems.getPath(rootPath);

		Path path = buildPath(fileExtension);
		Path dir = path.getParent();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		file = new File(path.getAbsolutePath());
		outputStream = new FileOutputStream(file);
	}

	public File getFile() {
		return file;
	}

	public String getRelativePath() {
		String path = rootPath.getAbsolutePath();
		return file.getAbsolutePath().substring(path.length() + 1);
	}

	/**
	 * 使用哈希值写文件内容
	 * @param bytes 文件内容字节数组
	 * @throws IOException
	 */
	@Override
	public void write(byte[] bytes) throws IOException {
		outputStream.write(bytes);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		outputStream.write(b, off, len);
	}

	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
	}

	@Override
	public void flush() throws IOException {
		outputStream.flush();
	}

	@Override
	public void close() throws IOException {
		outputStream.flush();
	}

	private synchronized Path buildPath(String fileExtension)
			throws IOException {
		String hash = hash();

		StringBuilder sb = new StringBuilder();

		sb.append(hash.substring(0, 1)).append("/");
		sb.append(hash.substring(1, 3)).append("/");
		sb.append(hash.substring(3, 5)).append("/");

		sb.append(hash.substring(5));
		if (!Strings.isNullOrEmpty(fileExtension)) {
			sb.append('.').append(fileExtension);
		}

		return rootPath.resolve(sb.toString());
	}

	private String hash() {
		String hex = UUID.getUUID32();
		BigInteger bi = new BigInteger(hex, 16);
		return Strings.padStart(bi.toString(36), 25, '0');
	}

}
