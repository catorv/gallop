package com.catorv.gallop.io.transport.support;


import com.catorv.gallop.io.transport.AbstractPath;
import com.catorv.gallop.io.transport.FileSystem;
import com.catorv.gallop.io.transport.Path;

import java.net.URI;

public class LocalFilePath extends AbstractPath {

	public LocalFilePath(String path) {
		super(new LocalFileSystem(path), path);
	}

	public LocalFilePath(LocalFileSystem fs, String path) {
		super(fs, path);
	}

	@Override
	protected Path createInstance(FileSystem fileSystem, URI uri) {
		return new LocalFilePath((LocalFileSystem) fileSystem, uri.getPath());
	}
}
