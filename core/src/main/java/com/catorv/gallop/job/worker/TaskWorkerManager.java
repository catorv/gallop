package com.catorv.gallop.job.worker;

import com.catorv.gallop.job.Callback;
import com.catorv.gallop.job.Task;
import com.google.inject.ImplementedBy;

@ImplementedBy(LocalTaskWorkerManager.class)
public interface TaskWorkerManager {

	<P, R> TaskWorker<P, R> lookup(Task<P, R> task, P input, Callback<R>... callbacks);

}
