package com.catorv.gallop.observable;

import com.google.common.base.Strings;
import com.google.inject.spi.InjectionListener;

import java.lang.reflect.Method;

/**
 * Observer Method Injection Observer
 * Created by cator on 8/1/16.
 */
public class ObserverInjectionListener implements InjectionListener<Object> {

	private ObservableManager manager;

	public ObserverInjectionListener(ObservableManager manager) {
		this.manager = manager;
	}

	@Override
	public void afterInjection(Object injectee) {
		for (Method method : injectee.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(Observe.class)) {
				Class<?>[] types = method.getParameterTypes();
				if (types.length == 1 && types[0] == Event.class) {
					Observe annotation = method.getAnnotation(Observe.class);
					String name = annotation.value();

					if (Strings.isNullOrEmpty(name)) {
						name = method.getName();
					}

					ObserverMethod observerMethod = new ObserverMethod(injectee, method);
					manager.getObservable(name).addObserver(observerMethod);
				}
			}
		}
	}

}
