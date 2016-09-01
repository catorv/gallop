package com.catorv.gallop.io;

import com.catorv.gallop.util.UUID;
import com.google.common.io.Files;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;

/**
 * Created by cator on 8/31/16.
 */
public class UdidFile extends File {

	public UdidFile(String pathname) {
		super(buildName(pathname, null));
	}

	public UdidFile(String parent, String child) {
		super(parent, buildName(child, new File(parent)));
	}

	public UdidFile(File parent, String child) {
		super(parent, buildName(child, parent));
	}

	public UdidFile(URI uri) {
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
		final String extname = Files.getFileExtension(pathname);
		return HashFile.buildPath(parent, UUID.getUUID25(), extname);
	}

}
