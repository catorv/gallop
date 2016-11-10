package com.catorv.gallop.job.schedule;

import com.catorv.gallop.lifecycle.MethodInvocation;
import org.quartz.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract Schedule Job
 * Created by cator on 8/10/16.
 */
public  class ScheduledJob implements Job {

	public static final String DATAKEY_AUTO_LOCK = "autoLock";
	public static final String DATAKEY_AUTO_SKIP = "autoSkip";
	public static final String DATAKEY_METHOD = "method";

	private static final Map<MethodInvocation, ReentrantLock> lockMap = new ConcurrentHashMap<>();
	private static final Map<MethodInvocation, Integer> runningMap = new ConcurrentHashMap<>();

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		final JobDetail jobDetail = context.getJobDetail();
		final JobDataMap dataMap = jobDetail.getJobDataMap();

		ReentrantLock lock = null;
		MethodInvocation methodInvocation = null;
		boolean autoSkip = false;
		boolean autoLock = true;
		try {
			Object method = dataMap.get(DATAKEY_METHOD);
			if (method != null && method instanceof MethodInvocation) {
				methodInvocation = (MethodInvocation) method;
			}
			//
			if (methodInvocation == null) return;

			if (dataMap.containsKey(DATAKEY_AUTO_SKIP)) {
				autoSkip = dataMap.getBooleanValue(DATAKEY_AUTO_SKIP);
			}
			//
			if (runningMap.containsKey(methodInvocation)) {
				if (autoSkip) return;
				//
				Integer running = runningMap.get(methodInvocation);
				runningMap.put(methodInvocation, running + 1);
			} else {
				runningMap.put(methodInvocation, 0);
			}

			if (dataMap.containsKey(DATAKEY_AUTO_LOCK)) {
				autoLock = dataMap.getBooleanValue(DATAKEY_AUTO_LOCK);
			}
			//
			if (autoLock) {
				lock = lockMap.get(methodInvocation);
				if (lock == null) {
					lock = new ReentrantLock();
					lockMap.put(methodInvocation, lock);
				}
				lock.lock();
			}

			methodInvocation.proceed();
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		} finally {
			if (lock != null) {
				lock.unlock();
			}
			if (runningMap.containsKey(methodInvocation)) {
				Integer running = runningMap.get(methodInvocation);
				if (running == 0) {
					runningMap.remove(methodInvocation);
				} else {
					runningMap.put(methodInvocation, running - 1);
				}
			}
		}
	}

}
