package com.catorv.gallop.io.lock;

import com.google.inject.Singleton;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class FileLockFactory {

	private final ConcurrentMap<String, FileLock> locks = new ConcurrentHashMap<>();

	private final static FileLockFactory factory = new FileLockFactory();

	private FileLockFactory() {

	}

	public static FileLockFactory getInstance() {
		return factory;
	}

	public FileLock getFileLock(File file) {
		String key = file.getAbsolutePath();
		FileLock lock = locks.get(key);
		if (lock != null) {
			return lock;
		}
		lock = new FileLockImpl(file);
		FileLock oLock = locks.putIfAbsent(file.getAbsolutePath(), lock);

		if (oLock != null) {
			return oLock;
		}
		return lock;
	}

}
