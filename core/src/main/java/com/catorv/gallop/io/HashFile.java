package com.catorv.gallop.io;

import com.catorv.gallop.util.DigestUtils;
import com.google.common.base.Strings;
import com.google.common.io.Files;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;

/**
 * Created by cator on 8/31/16.
 */
public class HashFile extends File {

	public HashFile(String pathname) {
		super(buildName(pathname, null));
	}

	public HashFile(String parent, String child) {
		super(parent, buildName(child, new File(parent)));
	}

	public HashFile(File parent, String child) {
		super(parent, buildName(child, parent));
	}

	public HashFile(URI uri) {
		super(buildUri(uri));
	}

	private static URI buildUri(URI uri) {
		try {
			return URI.create(buildName(uri.toURL().getFile(), null));
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static String buildName(String pathname, File parent) {
		final String hash = DigestUtils.hash(pathname);
		final String extname = Files.getFileExtension(pathname);
		return buildPath(parent, hash, extname);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	protected static String buildPath(File parent, String hash, String extname) {
		StringBuilder sb = new StringBuilder();

		sb.append(hash.substring(0, 1)).append(File.separator);
		sb.append(hash.substring(1, 3)).append(File.separator);
		sb.append(hash.substring(3, 5)).append(File.separator);

		File file;
		if (parent == null) {
			file = new File(sb.toString());
		} else {
			file = new File(parent, sb.toString());
		}
		if (!file.exists()) {
			file.mkdirs();
		}

		sb.append(hash.substring(5));
		if (!Strings.isNullOrEmpty(extname)) {
			sb.append('.').append(extname);
		}

		return sb.toString();
	}

}
