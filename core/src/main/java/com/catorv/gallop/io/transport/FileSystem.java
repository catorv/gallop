package com.catorv.gallop.io.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileSystem {

	public String getPartition();
	
	public String getScheme();
	
	public String getHost();
	
	public String[] getChild(String path) throws IOException;
	
	public String resolve(String path);
	
	public void write(String path, InputStream is) throws IOException;
	
	public void read(String path, OutputStream os) throws IOException;
	
	public boolean exists(String path) throws IOException;
	
	public Path mkdir(String parent, String name) throws IOException;
	
	public Path mkdirs(String parent, String path) throws IOException;
	
	public boolean isFile(String path) throws IOException;
	
	public boolean isDirectory(String path) throws IOException;
	
	public void remove(String path) throws IOException;
}
