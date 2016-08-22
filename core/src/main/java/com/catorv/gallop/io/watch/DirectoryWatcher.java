package com.catorv.gallop.io.watch;

import com.catorv.gallop.lifecycle.Destroy;
import com.catorv.gallop.log.InjectLogger;
import com.catorv.gallop.observable.ChangedObservable;
import com.google.inject.Singleton;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Directory Watcher
 * Created by cator on 8/5/16.
 */
@Singleton
public class DirectoryWatcher {

	@InjectLogger
	private Logger logger;

	private WatchService ws;

	private Map<WatchKey, ChangedObservable> watches = new ConcurrentHashMap<>();

	public DirectoryWatcher() throws IOException {

	}

	public synchronized void addObserver(DirectoryObserver observer) throws IOException {
		Path path = observer.getPath();
		if (path == null) {
			throw new IllegalArgumentException("the path of the observer is null.");
		}

		if (ws == null) {
			start();
		}

		WatchKey watchKey = path.register(ws, ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE);
		logger.info("listen on {}", path);

		ChangedObservable observable = watches.get(watchKey);
		if (observable == null) {
			observable = new ChangedObservable();
			watches.put(watchKey, observable);
		}
		observable.addObserver(observer);
	}

	public synchronized void deleteObserver(DirectoryObserver observer) throws IOException {
		Path path = observer.getPath();
		if (path == null) {
			return;
		}

		WatchKey watchKey = path.register(ws, ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE);

		ChangedObservable observable = watches.get(watchKey);
		if (observable != null) {
			observable.deleteObserver(observer);
			if (observable.countObservers() == 0) {
				watches.remove(watchKey);
				watchKey.cancel();

				if (watches.size() == 0 && ws != null) {
					stop();
				}
			}
		}

	}

	void processEvents() {
		while (true) {
			WatchKey watchKey;
			try {
				watchKey = ws.take();
			} catch (InterruptedException x) {
				break;
			}

			for (WatchEvent<?> event : watchKey.pollEvents()) {
				Kind<?> kind = event.kind();

				if (kind == OVERFLOW) {
					continue;
				}

				ChangedObservable observable = watches.get(watchKey);
				if (observable != null) {
					Path dir = (Path) watchKey.watchable();
					Path name = (Path) event.context();
					String fileName = dir.resolve(name).toString();
					observable.notifyObservers(new DirectoryEvent(fileName, kind));
				}
			}

			watchKey.reset();
		}
		try {
			stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void start() throws IOException {
		ws = FileSystems.getDefault().newWatchService();

		Thread thread = new Thread() {

			@Override
			public void run() {
				processEvents();
			}

		};
		thread.setDaemon(true);
		thread.start();
	}

	@Destroy
	private void stop() throws IOException {
		if (ws != null) {
			ws.close();
			ws = null;
		}
	}

}

