package com.catorv.gallop.job.schedule;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.reflect.Method;

public class ScheduledJobListener implements TypeListener {

	@Inject
	private ScheduleManager scheduleManager;

	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		Class<?> clazz = type.getRawType();
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(Schedule.class)) {
				Schedule schedule = method.getAnnotation(Schedule.class);
				String cron = getCron(schedule);
				if (cron == null) {
					continue;
				}
				encounter.register(new ScheduledJobInjector<I>(scheduleManager, method,
						schedule.desc(), cron, schedule.group(), schedule.autoLock()));
			}
		}
	}

	private String getCron(Schedule schedule) {
		if (!Strings.isNullOrEmpty(schedule.cron())) {
			return schedule.cron();
		}
		if (!Strings.isNullOrEmpty(schedule.value())) {
			return schedule.value();
		}
		return null;
	}

}
