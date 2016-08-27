package com.catorv.test.gallop.io.watch;

import com.catorv.gallop.io.watch.DirectoryEvent;
import com.catorv.gallop.io.watch.DirectoryObserver;
import com.catorv.gallop.io.watch.DirectoryWatcher;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.common.io.Files;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.Observable;

/**
 *
 * Created by cator on 8/8/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		LoggerModule.class
})
public class DirectoryWatcherTest {

	@Inject
	private DirectoryWatcher dw;

	static boolean hit = false;

	@Test
	public void testNotify() throws Exception {
		String home = System.getProperties().getProperty("user.home");

		dw.addObserver(new Listener(home + "/tmp"));
		dw.addObserver(new Listener(home + "/tmp/test"));

		File file = new File(home + "/tmp/test", "abc");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String parent = file.getParent();
			System.out.println("=======" + parent);
			File file2 = new File(parent, "abc.n");
			try {
				Files.move(file, file2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Thread.sleep(12000L);
		Assert.assertTrue(hit);
	}

	static class Listener extends DirectoryObserver {

		public Listener(String pathname) {
			super(pathname);
		}

		@Override
		public void update(Observable o, DirectoryEvent event) {
			System.out.println(event.getFileName());
			System.out.println(event.getKind());
			hit = true;
		}

	}

}
