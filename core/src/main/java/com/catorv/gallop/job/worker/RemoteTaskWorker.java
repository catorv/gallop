package com.catorv.gallop.job.worker;

import com.catorv.gallop.job.Callback;
import com.catorv.gallop.job.Task;

import java.net.URL;

public class RemoteTaskWorker<P, R> extends AbstractTaskWorker<P, R>{
	
	private URL endPoint;

	@SafeVarargs
	public RemoteTaskWorker(URL endPoint, Task<P, R> task, P input, Callback<R>... callbacks){
		super(task, input, callbacks);
		this.endPoint = endPoint;
	}
	
	@Override
	protected R runTask(Task<P, R> task, P input) throws Exception {
		//TODO replace real remote invoke process
		return task.execute(input);
	}
}
