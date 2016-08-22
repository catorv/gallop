package com.catorv.gallop.job.worker;

import com.catorv.gallop.job.ExecuteResult;

import java.util.concurrent.Callable;

public interface TaskWorker<P, R> extends Callable<ExecuteResult<R>> {

}
