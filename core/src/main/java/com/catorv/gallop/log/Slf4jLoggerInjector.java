package com.catorv.gallop.log;

import com.google.inject.MembersInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Logger对象注入器
 * Created by cator on 8/1/16.
 */
public class Slf4jLoggerInjector<T> implements MembersInjector<T> {

	private final Field field;
	private final Logger logger;

	Slf4jLoggerInjector(Field field) {
		this.field = field;
		logger = LoggerFactory.getLogger(field.getDeclaringClass());
		field.setAccessible(true);
	}

	@Override
	public void injectMembers(T ins) {
		try {
			field.set(ins, logger);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}

