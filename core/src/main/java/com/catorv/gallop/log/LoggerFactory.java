package com.catorv.gallop.log;

import com.google.inject.Singleton;
import org.slf4j.Logger;

/**
 * Logger工厂类
 * Created by cator on 8/1/16.
 */
@Singleton
public class LoggerFactory {

	public Logger getLogger(String name) {
		return org.slf4j.LoggerFactory.getLogger(name);
	}

	public Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}
}

