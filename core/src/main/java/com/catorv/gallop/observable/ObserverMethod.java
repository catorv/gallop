package com.catorv.gallop.observable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by cator on 8/1/16.
 */
public class ObserverMethod implements Observer {

	/** 监听方法所属的上下文对象 */
	private Object context;
	/** 监听方法 */
	private Method method;

	public ObserverMethod(Object context, Method method) {
		this.context = context;
		this.method = method;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Event) {
			Event event = (Event) arg;
			try {
				method.invoke(context, event);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public Object getContext() {
		return context;
	}

	public void setContext(Object context) {
		this.context = context;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
}
