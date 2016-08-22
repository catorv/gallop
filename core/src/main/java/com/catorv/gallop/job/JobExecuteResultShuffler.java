package com.catorv.gallop.job;

/**
 * Job Execute Result Shuffler
 * Created by cator on 8/11/16.
 */
public interface JobExecuteResultShuffler<P, R>
		extends ExecuteResultShuffler<JobContext<P, R>, JobContext<P, R>> {
}
