package com.catorv.gallop.log;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.slf4j.Logger;

import java.lang.reflect.Field;

/**
 * InjectLogger监听器
 * Created by cator on 8/1/16.
 */
public class Slf4jLoggerInjectListener implements TypeListener {

	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		Class<?> clazz = type.getRawType();
		while (clazz != null && clazz != Object.class) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getType() == Logger.class && field.isAnnotationPresent(InjectLogger.class)) {
					encounter.register(new Slf4jLoggerInjector<I>(field));
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

}

