package com.catorv.gallop.io.transport.support;

import com.catorv.gallop.io.transport.AbstractPath;
import com.catorv.gallop.io.transport.FileSystem;
import com.catorv.gallop.io.transport.Path;

import java.net.URI;

public class FtpPath extends AbstractPath {

	public FtpPath(String host, int port, String username, String password,
	               String path) {
		super(new FtpFileSystem(host, port, username, password), path);
	}

	public FtpPath(FtpFileSystem fs, String path) {
		super(fs, path);
	}

	@Override
	protected Path createInstance(FileSystem fileSystem, URI uri) {
		return new FtpPath((FtpFileSystem) fileSystem, uri.getPath());
	}

}
