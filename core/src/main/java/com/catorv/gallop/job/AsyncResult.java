package com.catorv.gallop.job;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Async Result
 * Created by cator on 8/11/16.
 */
public class AsyncResult<T> implements Future<T> {

	private T object;

	private Future<AsyncResult<T>> future;

	AsyncResult(T object) {
		this.object = object;
	}

	AsyncResult(Future<AsyncResult<T>> future) {
		this.future = future;
	}

	public static <O> AsyncResult<O> of(O object) {
		return new AsyncResult<>(object);
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return object == null && future.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return object == null && future.isCancelled();
	}

	@Override
	public boolean isDone() {
		return object != null || future.isDone();
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		return object == null ? future.get().object : object;
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return object == null ? future.get(timeout, unit).object : object;
	}
}
