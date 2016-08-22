package com.catorv.gallop.job;

/**
 *
 * Created by cator on 8/9/16.
 */
public interface Callback<R> {

	void call(ExecuteResult<R> result);

}

