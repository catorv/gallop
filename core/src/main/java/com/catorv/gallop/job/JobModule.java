package com.catorv.gallop.job;

import com.catorv.gallop.job.schedule.ScheduleManager;
import com.catorv.gallop.job.schedule.ScheduledJobListener;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * Schedule Module
 * 依赖模块:
 *  ConfigurationModule
 *  LifecycleModule
 *  LoggerModule
 * Created by cator on 8/10/16.
 */
public class JobModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ScheduleManager.class).asEagerSingleton();

		ScheduledJobListener scheduledJobListener = new ScheduledJobListener();
		requestInjection(scheduledJobListener);
		bindListener(Matchers.any(), scheduledJobListener);

		AsyncInterceptor asyncInterceptor = new AsyncInterceptor();
		requestInjection(asyncInterceptor);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Async.class),
				asyncInterceptor);
	}
}
