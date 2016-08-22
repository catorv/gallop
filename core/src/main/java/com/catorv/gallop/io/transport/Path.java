package com.catorv.gallop.io.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public interface Path extends Comparable<Path>, Iterable<Path> {
	
	public boolean isFile() throws IOException;
	
	public boolean isDirectory() throws IOException;

	public Path getParent();

	public Path getRoot();
	
	public boolean exists() throws IOException;
	
	public Path mkdir(String name) throws IOException;
	
	public Path mkdirs(String path) throws IOException;
	
	public Path mkdirs() throws IOException;

	public boolean isAbsolute();

	public Path normalize();

	public URI toUri();

	public String getName();
	
	public String getAbsolutePath();

	public FileSystem getFileSystem();
	
	public void write(InputStream is) throws IOException;
	
	public void read(OutputStream os) throws IOException;
	
	public void remove() throws IOException;
	
	public Path resolve(String path) throws IOException;
	
}
