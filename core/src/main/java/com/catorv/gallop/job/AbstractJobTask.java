package com.catorv.gallop.job;

/**
 * Abstract Job Task
 * Created by cator on 8/10/16.
 */
public abstract class AbstractJobTask<P, R> extends AbstractTask<JobContext<P, R>, JobContext<P, R>> {

	@Override
	public final JobContext<P, R> execute(JobContext<P, R> context) throws Exception {
		R result =  execute(context.getInput(), context);
		context.setResult(result);
		return context;
	}

	abstract public R execute(P input, JobContext<P, R> context) throws Exception;
}
