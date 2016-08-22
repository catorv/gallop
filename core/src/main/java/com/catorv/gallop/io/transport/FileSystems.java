package com.catorv.gallop.io.transport;


import com.catorv.gallop.io.transport.support.FtpPath;
import com.catorv.gallop.io.transport.support.LocalFilePath;
import com.catorv.gallop.io.transport.support.SftpPath;

import java.net.URI;
import java.net.URISyntaxException;

public class FileSystems {

	public static Path getPath(String path) throws URISyntaxException {
		return getPath(path, null);
	}

	public static Path getPath(String path, String privateKey)
			throws URISyntaxException {
		int index = path.indexOf('@');

		URI uri = new URI(index > -1 ? path.substring(index + 1) : path);
		String scheme = uri.getScheme();

		String identity = index > -1 ? path.substring(0, index) : "";
		index = identity.indexOf(':');
		String username = index > -1 ? identity.substring(0, index) : identity;
		String password = index > -1 ? identity.substring(index + 1) : "";

		if ("sftp".equals(scheme)) {
			return new SftpPath(uri.getHost(), uri.getPort(), username, privateKey,
					uri.getPath());
		} else if ("ftp".equals(scheme)) {
			return new FtpPath(uri.getHost(), uri.getPort(), username, password,
					uri.getPath());
		} else {
			return new LocalFilePath(uri.getPath());
		}
	}
}
