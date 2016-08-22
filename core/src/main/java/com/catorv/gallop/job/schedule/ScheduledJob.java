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
	public static final String DATAKEY_METHOD = "method";

	private static final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		ReentrantLock lock = null;
		try {
			JobDetail jobDetail = context.getJobDetail();
			JobDataMap dataMap = jobDetail.getJobDataMap();

			MethodInvocation methodInvocation = null;
			Object method = dataMap.get(DATAKEY_METHOD);
			if (method instanceof MethodInvocation) {
				methodInvocation = (MethodInvocation) method;
			}

			if (methodInvocation == null) {
				return;
			}

			boolean autoLock = true;
			if (dataMap.containsKey(DATAKEY_AUTO_LOCK)) {
				autoLock = dataMap.getBooleanValue(DATAKEY_AUTO_LOCK);
			}

			if (autoLock) {
				JobKey jobKey = jobDetail.getKey();
				String key = jobKey.getName() + "@" + jobKey.getGroup();
				lock = lockMap.get(key);
				if (lock == null) {
					lock = new ReentrantLock();
					lockMap.put(key, lock);
				}
			}

			if (lock != null) {
				lock.lock();
			}

			methodInvocation.proceed();
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		} finally {
			if (lock != null) {
				lock.unlock();
			}
		}
	}

}
