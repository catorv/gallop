package com.catorv.gallop.observable;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * Event Module
 * Created by cator on 8/1/16.
 */
public class ObservableModule extends AbstractModule {

	private static ObservableManager manager = new ObservableManager();

	@Override
	protected void configure() {
		this.bind(ObservableManager.class).toInstance(manager);

		ObserverInjectionListener injectionListener = new ObserverInjectionListener(manager);
		ObserverTypeLisenter listener = new ObserverTypeLisenter(injectionListener);
		bindListener(Matchers.any(), listener);
	}

}
