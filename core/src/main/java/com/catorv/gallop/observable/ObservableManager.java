package com.catorv.gallop.observable;

import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cator on 8/23/16.
 */
public class ObservableManager {

	private ConcurrentHashMap<String, ChangedObservable> observables = new ConcurrentHashMap<>();

	public Observable getObservable(String name) {
		ChangedObservable observable = observables.get(name);
		if (observable == null) {
			observable = new ChangedObservable();
			observables.put(name, observable);
		}
		return observable;
	}

	public void notifyObservers(Event event) {
		Observable observable = observables.get(event.getName());
		if (observable != null) {
			observable.notifyObservers(event);
		}
	}

}
