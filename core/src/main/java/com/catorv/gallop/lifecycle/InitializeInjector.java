package com.catorv.gallop.lifecycle;

import com.google.inject.MembersInjector;

import java.lang.reflect.Method;

/**
 * 初始化事件注入器
 * Created by cator on 6/21/16.
 */
class InitializeInjector<T> implements MembersInjector<T> {

	private Method method;

	InitializeInjector(Method method){
		this.method = method;
	}

	@Override
	public void injectMembers(T ins) {
		try {
			method.setAccessible(true);
			method.invoke(ins);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

