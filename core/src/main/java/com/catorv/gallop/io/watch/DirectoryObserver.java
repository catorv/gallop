package com.catorv.gallop.io.watch;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

/**
 * Watch Observer
 * Created by cator on 8/8/16.
 */
public abstract class DirectoryObserver implements Observer {

	private Path path;

	public DirectoryObserver(String pathname) {
		this(new File(pathname));
	}

	public DirectoryObserver(File dir) {
		if (dir.exists() || dir.mkdirs()) {
			path = Paths.get(dir.getAbsolutePath());
		}
	}

	public Path getPath() {
		return path;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof DirectoryEvent) {
			update(o, (DirectoryEvent) arg);
		}
	}

	public abstract void update(Observable o, DirectoryEvent event);

}
