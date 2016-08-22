package com.catorv.gallop.observable;

import java.util.Observable;

/**
 * Directory Observable
 * Created by cator on 8/8/16.
 */
public class ChangedObservable extends Observable {
	@Override
	public void notifyObservers(Object arg) {
		setChanged();
		super.notifyObservers(arg);
	}
}
