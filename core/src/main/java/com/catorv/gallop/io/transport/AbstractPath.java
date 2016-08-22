package com.catorv.gallop.io.transport;

import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractPath implements Path {

	private List<String> parts;

	private String partition;

	private boolean absolute;

	private FileSystem fs;

	private String absolutePath;

	protected AbstractPath(FileSystem fileSystem, String path) {
		this.fs = fileSystem;
		absolutePath = fs.resolve(path);

		partition = fs.getPartition();
		if (Strings.isNullOrEmpty(partition)) {
			partition = "";
		}

		path = path.replace(File.pathSeparatorChar, '/');
		if (path.startsWith("/") || path.startsWith(partition) ||
				path.startsWith("/" + partition)) {
			absolute = true;
		}

		parts = new ArrayList<>();
		int s = partition.length();
		int t;
		while (true) {
			t = s + 1;
			s = absolutePath.indexOf('/', t);
			if (s < 0) {
				if (t < absolutePath.length()) {
					cd(absolutePath.substring(t));
				}
				break;
			}
			cd(absolutePath.substring(t, s));
		}
	}

	private void cd(String p) {
		if ("..".equals(p)) {
			if (parts.size() > 0) {
				parts.remove(parts.size() - 1);
			}
		} else if (p.length() > 0 && !".".equals(p)) {
			parts.add(p);
		}
	}

	@Override
	public Iterator<Path> iterator() {
		try {
			List<Path> paths = new ArrayList<>();
			List<String> names = new ArrayList<>();
			names.addAll(this.parts);
			for (String name : fs.getChild(absolutePath)) {
				names.add(URLEncoder.encode(name, "utf-8"));
				paths.add(createInstance(fs, toUri(names)));
				names.remove(names.size() - 1);
			}
			return paths.iterator();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int compareTo(Path path) {
		if (path == null) {
			return 1;
		}
		return this.toUri().compareTo(path.toUri());
	}

	@Override
	public Path getParent() {
		return createInstance(fs, toUri(parts, parts.size() - 1));
	}

	@Override
	public Path getRoot() {
		return createInstance(fs, toUri(parts, 0));
	}

	@Override
	public boolean isAbsolute() {
		return absolute;
	}

	@Override
	public Path normalize() {
		return createInstance(fs, toUri());
	}

	@Override
	public URI toUri() {
		return toUri(parts);
	}

	private URI toUri(List<String> parts) {
		return toUri(parts, parts.size());
	}

	private URI toUri(List<String> parts, int index) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(fs.getScheme());
			sb.append("://");
			if (fs.getHost() != null) {
				sb.append(fs.getHost());
			}
			if (!Strings.isNullOrEmpty(partition)) {
				sb.append('/');
				sb.append(partition);
			}
			for (int i = 0; i < index; i++) {
				sb.append('/');
				sb.append(parts.get(i));
			}
			return new URI(sb.toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getName() {
		return parts.get(parts.size() - 1);
	}

	@Override
	public String getAbsolutePath() {
		return absolutePath;
	}

	@Override
	public String toString() {
		return this.toUri().toString();
	}

	@Override
	public FileSystem getFileSystem() {
		return fs;
	}

	@Override
	public void write(InputStream is) throws IOException {
		this.fs.write(absolutePath, is);
	}

	@Override
	public void read(OutputStream os) throws IOException {
		this.fs.read(absolutePath, os);
	}

	@Override
	public boolean exists() throws IOException {
		return this.getFileSystem().exists(this.getAbsolutePath());
	}

	@Override
	public Path mkdir(String name) throws IOException {
		return this.getFileSystem().mkdir(this.getAbsolutePath(), name);
	}

	@Override
	public Path mkdirs(String path) throws IOException {
		return this.getFileSystem().mkdirs(this.getAbsolutePath(), path);
	}

	@Override
	public Path mkdirs() throws IOException {
		return this.getFileSystem().mkdirs(this.getAbsolutePath(), "");
	}

	@Override
	public boolean isFile() throws IOException {
		return this.getFileSystem().isFile(this.getAbsolutePath());
	}

	@Override
	public boolean isDirectory() throws IOException {
		return this.getFileSystem().isDirectory(this.getAbsolutePath());
	}

	@Override
	public void remove() throws IOException {
		this.getFileSystem().remove(this.getAbsolutePath());
	}

	@Override
	public Path resolve(String path) throws IOException {
		List<String> parts = new ArrayList<>(this.parts.size() + 1);
		parts.addAll(this.parts);
		parts.add(path);
		return createInstance(fs, this.toUri(parts));
	}

	protected abstract Path createInstance(FileSystem fileSystem, URI uri);
}
