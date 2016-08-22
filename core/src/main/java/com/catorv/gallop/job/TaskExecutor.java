package com.catorv.gallop.job;

import com.catorv.gallop.cfg.Configuration;
import com.catorv.gallop.job.worker.TaskWorkerManager;
import com.catorv.gallop.lifecycle.Destroy;
import com.catorv.gallop.lifecycle.Initialize;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.*;

@Singleton
public class TaskExecutor implements Closeable {

	private ExecutorService executor;

	@Configuration("task.executor")
	private TaskExecutorConfiguration configuration;

	@Inject
	private TaskWorkerManager taskWorkerManager;

	@Initialize
	public void init() {
		executor = new ThreadPoolExecutor(
				configuration.getMinimumPoolSize(),
				configuration.getMaximumPoolSize(),
				configuration.getKeepAliveTime(),
				TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(configuration.getWorkingQueueSize())
		);
	}

	public <P, R> Future<ExecuteResult<R>> submit(Task<P, R> task, P input,
	                                              Callback<R>... callbacks) {
		return submit(taskWorkerManager.lookup(task, input, callbacks));
	}

	public <R> Future<R> submit(Callable<R> callable) {
		return executor.submit(callable);
	}

	@Destroy
	@Override
	public void close() throws IOException {
		this.executor.shutdown();
	}

}
