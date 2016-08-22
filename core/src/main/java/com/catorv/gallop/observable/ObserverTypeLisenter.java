package com.catorv.gallop.observable;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * Observer Type Listener
 * Created by cator on 8/1/16.
 */
public class ObserverTypeLisenter implements TypeListener {

	private ObserverInjectionListener injectionListener;

	public ObserverTypeLisenter(ObserverInjectionListener injectionListener) {
		this.injectionListener = injectionListener;
	}

	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		Class<?> clazz = type.getRawType();
		while (clazz != null && clazz != Object.class) {
			if (clazz.isAnnotationPresent(Observing.class)) {
				encounter.register(injectionListener);
			}
			clazz = clazz.getSuperclass();
		}
	}

}
