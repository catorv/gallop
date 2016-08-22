package com.catorv.gallop.job.worker;

import com.catorv.gallop.job.Callback;
import com.catorv.gallop.job.ExecuteResult;
import com.catorv.gallop.job.Task;

public abstract class AbstractTaskWorker<P, R> implements TaskWorker<P, R> {

	private Task<P, R> task;

	private Callback<R>[] callbacks;

	private P input;

	@SafeVarargs
	protected AbstractTaskWorker(Task<P, R> task, P input, Callback<R>... callbacks) {
		this.task = task;
		this.callbacks = callbacks;
		this.input = input;
	}

	@Override
	public ExecuteResult<R> call() throws Exception {
		ExecuteResult<R> result;
		try {
			result = new ExecuteResult<>(runTask(task, input));
		} catch (Exception e) {
			result = new ExecuteResult<>(e);
			e.printStackTrace();
		}

		if (callbacks != null) {
			for (Callback<R> cb : callbacks) {
				cb.call(result);
			}
		}
		return result;
	}

	protected abstract R runTask(Task<P, R> task, P input) throws Exception;
}
