package com.catorv.gallop.lifecycle;

import com.google.inject.MembersInjector;

import java.lang.reflect.Method;

/**
 * 注销时间注入器
 * Created by cator on 6/21/16.
 */
class ShutdownInjector<T> implements MembersInjector<T> {

	private Method method;

	ShutdownInjector(Method method){
		this.method = method;
	}

	@Override
	public void injectMembers(T ins) {
		try {
			ShutdownHook.register(new MethodInvocation(method, ins));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}