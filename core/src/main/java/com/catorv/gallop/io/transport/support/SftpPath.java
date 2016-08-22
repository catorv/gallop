package com.catorv.gallop.io.transport.support;

import com.catorv.gallop.io.transport.AbstractPath;
import com.catorv.gallop.io.transport.FileSystem;
import com.catorv.gallop.io.transport.Path;

import java.net.URI;

public class SftpPath extends AbstractPath {

	public SftpPath(String host, int port, String username, String privateKey,
	                String path) {
		super(new SftpFileSystem(host, port, username, privateKey), path);
	}

	public SftpPath(SftpFileSystem fs, String path) {
		super(fs, path);
	}

	@Override
	protected Path createInstance(FileSystem fileSystem, URI uri) {
		return new SftpPath((SftpFileSystem) fileSystem, uri.getPath());
	}

}
