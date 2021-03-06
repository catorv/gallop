package com.catorv.gallop.job.schedule;

import com.catorv.gallop.lifecycle.MethodInvocation;
import com.google.inject.MembersInjector;

import java.lang.reflect.Method;

public class ScheduledJobInjector<T> implements MembersInjector<T> {

	private ScheduleManager scheduleManager;

	private String cronExpression;

	private String desc;

	private String group;

	private Method method;

	private boolean autoLock;
	private boolean autoSkip;

	public ScheduledJobInjector(ScheduleManager scheduleManager,
	                            Method method, String desc,
	                            String cronExpression, String group,
	                            boolean autoLock, boolean autoSkip) {
		this.scheduleManager = scheduleManager;
		this.method = method;
		this.desc = desc;
		this.cronExpression = cronExpression.trim().replaceAll("\\s+", " ");
		this.group = group;
		this.autoLock = autoLock;
		this.autoSkip = autoSkip;
	}

	@Override
	public void injectMembers(T ins) {
		try {
			MethodInvocation mi = new MethodInvocation(method, ins);
			scheduleManager.schedule(cronExpression, mi, desc, group, autoLock, autoSkip);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}