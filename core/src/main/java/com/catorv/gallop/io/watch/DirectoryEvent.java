package com.catorv.gallop.io.watch;

import java.nio.file.WatchEvent;

/**
 * Watch Event
 * Created by cator on 8/8/16.
 */
public class DirectoryEvent {
	private final String fileName;
	private final WatchEvent.Kind<?> kind;

	public DirectoryEvent(String fileName, WatchEvent.Kind<?> kind) {
		this.fileName = fileName;
		this.kind = kind;
	}

	public String getFileName() {
		return fileName;
	}

	@SuppressWarnings("rawtypes")
	public WatchEvent.Kind getKind() {
		return kind;
	}
}
