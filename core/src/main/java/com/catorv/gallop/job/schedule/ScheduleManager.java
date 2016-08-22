package com.catorv.gallop.job.schedule;

import com.catorv.gallop.cfg.Configuration;
import com.catorv.gallop.job.IDGenerator;
import com.catorv.gallop.job.TaskExecutor;
import com.catorv.gallop.lifecycle.Destroy;
import com.catorv.gallop.lifecycle.Initialize;
import com.catorv.gallop.lifecycle.MethodInvocation;
import com.catorv.gallop.log.InjectLogger;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * Schedule Manager
 * Created by cator on 8/10/16.
 */
@Singleton
public class ScheduleManager {

	@Configuration("schedule")
	private ScheduleConfiguration config;

	@Inject
	private TaskExecutor taskExecutor;

	@InjectLogger
	private Logger logger;

	private Scheduler scheduler;

	@Initialize
	public void init() {
		SchedulerFactory schedulerFactory;
		Properties properties = loadPropertis(config.getQuartzConfigFile());
		try {
			if (properties == null) {
				schedulerFactory = new StdSchedulerFactory();
			} else {
				schedulerFactory = new StdSchedulerFactory(properties);
			}

			scheduler = schedulerFactory.getScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Destroy
	public void destroy() throws SchedulerException {
		if (scheduler != null) {
			scheduler.shutdown();
		}
	}

	public void scheduleJob(JobDetail job, Trigger trigger)
			throws SchedulerException {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.scheduleJob(job, trigger);
		} else {
			logger.warn("scheduler is null or has been shutdown.");
		}
	}

	public void schedule(String cronExpression,
	                     MethodInvocation methodInvocation, String desc,
	                     String group, boolean autoLock)
			throws SchedulerException {
		Trigger trigger = buildTrigger(cronExpression, group);
		JobDetail job = buildJob(methodInvocation, desc, group, autoLock);
		scheduleJob(job, trigger);
	}

	public void schedule(String cronExpression, MethodInvocation methodInvocation)
			throws SchedulerException {
		schedule(cronExpression, methodInvocation, "", "default", true);
	}

	public Trigger buildTrigger(String cronExpression, String group) {
//		String name = DigestUtils.hash(cronExpression + "@" + group);
		String name = IDGenerator.nextJobID();
		return TriggerBuilder.newTrigger()
				.withIdentity(name, group)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
				.build();
	}

	public JobDetail buildJob(MethodInvocation methodInvocation, String desc,
	                          String group, boolean autoLock) {
//		String name = DigestUtils.hash(methodInvocation.getMethod().toGenericString() + "@" + group);
		String name = IDGenerator.nextJobID();
		JobDetail job = JobBuilder.newJob(ScheduledJob.class)
				.withIdentity(name, group)
				.withDescription(desc)
				.build();

		JobDataMap dataMap = job.getJobDataMap();
		dataMap.put(ScheduledJob.DATAKEY_METHOD, methodInvocation);
		dataMap.put(ScheduledJob.DATAKEY_AUTO_LOCK, autoLock);

		return job;
	}

	private Properties loadPropertis(String pathname) {
		if (!Strings.isNullOrEmpty(pathname)) {
			InputStream is = null;
			File file = new File(pathname);
			if (file.exists()) {
				try {
					is = new FileInputStream(file);
				} catch (FileNotFoundException ignored) {
					// nothing
				}
			} else {
				is = this.getClass().getClassLoader().getResourceAsStream(pathname);
			}
			if (is != null) {
				Properties properties = new Properties();
				try {
					properties.load(is);
					is.close();
					return properties;
				} catch (IOException e) {
					// nothing
				}
			}
		}
		return null;
	}

}
