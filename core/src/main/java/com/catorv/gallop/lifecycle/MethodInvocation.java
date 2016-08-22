package com.catorv.gallop.lifecycle;

import java.lang.reflect.Method;

/**
 * 方法调用信息
 * Created by cator on 6/21/16.
 */
public class MethodInvocation {

	private Object[] arguments;

	private Object othis;

	private Method method;

	public MethodInvocation(Method method, Object othis, Object... arguments) {
		this.arguments = arguments;
		this.method = method;
		this.othis = othis;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public Object getThis() {
		return othis;
	}

	public Object proceed() throws Throwable {
		method.setAccessible(true);
		return method.invoke(othis, arguments);
	}

	public Method getMethod() {
		return this.method;
	}

	public String hashValue() {
		StringBuilder sb = new StringBuilder();
		sb.append(othis.getClass().getName()).append('#').append(method.getName());
		if (arguments != null && arguments.length > 0) {
			for (Object argument : arguments) {
				sb.append(':').append(argument.getClass().getName());
			}
		}
		return null;
	}

}