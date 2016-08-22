package com.catorv.gallop.job.worker;

import com.catorv.gallop.job.Callback;
import com.catorv.gallop.job.Task;

public class LocalTaskWorkerManager implements TaskWorkerManager {

	@Override
	public <P, R> TaskWorker<P, R> lookup(Task<P, R> task, P input,
	                                      Callback<R>... callbacks) {
		return new LocalTaskWorker<>(task, input, callbacks);
	}

}
