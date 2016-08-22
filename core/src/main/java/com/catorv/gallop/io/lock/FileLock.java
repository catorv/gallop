package com.catorv.gallop.io.lock;

import java.io.IOException;

public interface FileLock {

	public void lock() throws IOException, InterruptedException;
	
	public void unlock() throws IOException;
}
