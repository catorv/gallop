package com.catorv.gallop.io.lock;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class FileLockImpl implements FileLock {

	private File lockFile;
	private java.nio.channels.FileLock fileLock;
	private FileChannel fc;
	private RandomAccessFile raf;

	private final WriteLock lock = (new ReentrantReadWriteLock()).writeLock();

	public FileLockImpl(File file) {
		lockFile = new File(file.getParent(), file.getName() + ".lck");
		lockFile.deleteOnExit();
	}

	public void lock() throws IOException, InterruptedException {
		// fileLock within JVM
		lock.lock();
		raf = new RandomAccessFile(lockFile.getAbsoluteFile(), "rws");
		fc = raf.getChannel();

		// fileLock between JVMs
		while (fileLock == null) {
			try {
				fileLock = fc.lock();
			} catch (OverlappingFileLockException ignored) {
				Thread.sleep(100L);
			}
		}
	}

	public void unlock() throws IOException {
		lock.lock();
		try {
			if (fileLock != null) {
				fileLock.release();
			}
			if (raf != null) {
				raf.close();
			}
			if (fc != null) {
				fc.close();
			}
		} finally {
			fileLock = null;
			raf = null;
			fc = null;
			lock.unlock();
			lock.unlock();
		}
	}
}
