package com.catorv.gallop.lifecycle;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.reflect.Method;

/**
 * 对象生命周期管理注解监听器
 * Created by cator on 6/21/16.
 */
class LifecycleListener implements TypeListener {

	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		Class<?> clazz = type.getRawType();
		while (clazz != null && clazz != Object.class) {
			for (Method method : clazz.getMethods()) {
				if (method.isAnnotationPresent(Initialize.class)) {
					encounter.register(new InitializeInjector<I>(method));
				} else if (method.isAnnotationPresent(Destroy.class)) {
					encounter.register(new ShutdownInjector<I>(method));
				}
			}
			clazz = clazz.getSuperclass();
		}
	}
}
