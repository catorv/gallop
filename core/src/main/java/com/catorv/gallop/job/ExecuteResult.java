package com.catorv.gallop.job;

public class ExecuteResult<R> {
	private R result;

	private Exception exception;

	public ExecuteResult(R result) {
		this.result = result;
	}

	public ExecuteResult(Exception exception) {
		this.exception = exception;
	}

	public R get() {
		return result;
	}

	public Exception getException() {
		return exception;
	}

}