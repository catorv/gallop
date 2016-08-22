package com.catorv.gallop.job;

public class TaskExecutorConfiguration {

	/** the minimum number of threads to allow in the pool */
	private int minimumPoolSize;

	/** the maximum number of threads to allow in the pool */
	private int maximumPoolSize;

	/**
	 * (Seconds)
	 * when the number of threads is greater than the core,
	 * this is the maximum time that excess idle threads
	 * will wait for new tasks before terminating.
	 */
	private long keepAliveTime;

	/** the capacity of the queue */
	private int workingQueueSize;

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public int getWorkingQueueSize() {
		return workingQueueSize;
	}

	public void setWorkingQueueSize(int workingQueueSize) {
		this.workingQueueSize = workingQueueSize;
	}

	public int getMinimumPoolSize() {
		return minimumPoolSize;
	}

	public void setMinimumPoolSize(int minimumPoolSize) {
		this.minimumPoolSize = minimumPoolSize;
	}
}
