package com.catorv.gallop.io.lock;

import com.catorv.gallop.test.junit.GuiceTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 *
 * Created by cator on 8/5/16.
 */
@RunWith(GuiceTestRunner.class)
public class FileLockTest {

	private long total = 0L;

	@Test
	public void testLock() throws Exception {
		final FileLockFactory factory = FileLockFactory.getInstance();
		int loops = 10;
		final CountDownLatch latch = new CountDownLatch(loops);
		final long sleep = 300L;

		final File file = File.createTempFile(FileLock.class.getName(), "");
		for (int i = 0; i < loops; i++) {
			(new Thread() {

				@Override
				public void run() {
					FileLock lock = factory.getFileLock(file);
					try {
						lock.lock();
						long d = System.currentTimeMillis();
						Thread.sleep(sleep);
						total += System.currentTimeMillis() - d;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							lock.unlock();
						} catch (Exception e) {
							e.printStackTrace();
						}
						latch.countDown();
					}
				}

			}).start();
		}
		latch.await();

		File lockFile = new File(file.getParent(), file.getName() + ".lck");
		Assert.assertTrue(lockFile.exists());
		Assert.assertTrue(total >= sleep * loops);
	}

	public static void main(String args[]) throws Exception {
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		WriteLock writeLock = lock.writeLock();
		writeLock.lock();
		writeLock.unlock();
	}
}

