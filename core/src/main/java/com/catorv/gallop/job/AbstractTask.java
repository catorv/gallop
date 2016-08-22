package com.catorv.gallop.job;

public abstract class AbstractTask<P, R> implements Task<P, R> {

	private String id;

	protected AbstractTask() {
		this.id = IDGenerator.nextTaskID();
	}

	@Override
	public String getId() {
		return id;
	}

}
