package com.catorv.gallop.job.worker;

import com.catorv.gallop.job.Callback;
import com.catorv.gallop.job.Task;

public class LocalTaskWorker<P, R> extends AbstractTaskWorker<P, R>{

	@SafeVarargs
	public LocalTaskWorker(Task<P, R> task, P input, Callback<R>... callbacks){
		super(task, input, callbacks);
	}
	
	@Override
	protected R runTask(Task<P, R> task, P input) throws Exception {
		return task.execute(input);
	}

}
