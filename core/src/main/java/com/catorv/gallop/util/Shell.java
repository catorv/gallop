package com.catorv.gallop.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 运行Shell脚本
 * Created by cator on 8/2/16.
 */
public class Shell {

	private ProcessBuilder pb = null;

	private long timeout = -1L;

	public static ShellBuilder command(String shell) {
		return new ShellBuilder().invoke(shell);
	}

	private Shell(ProcessBuilder pb, long timeout) {
		this.pb = pb;
		this.timeout = timeout;
	}

	public int execute() throws InterruptedException, IOException {
		return execute(null);
	}

	public int execute(OutputStream os) throws InterruptedException, IOException {
		Process process = pb.start();
		int exitValue = waitFor(process);

		if (os != null) {
			try {
				StreamUtils.copyStream(process.getInputStream(), os);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return exitValue;
	}

	private int waitFor(Process process) throws InterruptedException {
		if (timeout <= 0) {
			return process.waitFor();
		}

		long startTime = System.nanoTime();
		TimeUnit unit = TimeUnit.SECONDS;
		long rem = unit.toNanos(timeout);

		do {
			try {
				return process.exitValue();
			} catch(IllegalThreadStateException ex) {
				if (rem > 0) {
					Thread.sleep(Math.min(TimeUnit.NANOSECONDS.toMillis(rem) + 1, 100));
				}
			}
			rem = unit.toNanos(timeout) - (System.nanoTime() - startTime);
		} while (rem > 0);

		process.destroy();
		return -1;
	}

	/**
	 * Shell对象构建器
	 */
	public static class ShellBuilder {

		private ProcessBuilder pb = new ProcessBuilder();

		private List<String> commands = new ArrayList<String>();

		private long timeout = -1L;

		public ShellBuilder invoke(String shell) {
			commands.add(shell);
			return this;
		}

		public ShellBuilder args(String... parameters) {
			Collections.addAll(commands, parameters);
			return this;
		}

		public ShellBuilder args(List<String> parameters) {
			for (String p : parameters) {
				commands.add(p);
			}
			return this;
		}

		public ShellBuilder timeout(long timeout) {
			this.timeout = timeout;
			return this;
		}

		public ShellBuilder directory(File dir) {
			pb.directory(dir);
			return this;
		}

		public Shell build() {
			return new Shell(pb.command(commands), timeout);
		}

		public int execute(OutputStream os) throws IOException, InterruptedException {
			return build().execute(os);
		}

		public int execute() throws IOException, InterruptedException {
			return execute(null);
		}
	}
}

