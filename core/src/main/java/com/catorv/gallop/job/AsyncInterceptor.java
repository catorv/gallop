package com.catorv.gallop.job;

import com.google.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Async Interceptor
 * Created by cator on 8/11/16.
 */
public class AsyncInterceptor implements MethodInterceptor {

	@Inject
	private TaskExecutor taskExecutor;

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Method method = methodInvocation.getMethod();
		Class<?> returnType = method.getReturnType();
		if (void.class == returnType) {
			invokeVoidMethod(methodInvocation);
			return null;
		}
		if (Future.class.isAssignableFrom(returnType)) {
			return invoke((Class<? extends Future<Object>>) returnType, methodInvocation);
		}
		return methodInvocation.proceed();
	}

	private void invokeVoidMethod(final MethodInvocation methodInvocation) {
		taskExecutor.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				try {
					methodInvocation.proceed();
				} catch (Throwable throwable) {
					throw new RuntimeException(throwable);
				}
				return null;
			}
		});
	}

	private <T> Future<T> invoke(Class<? extends Future<T>> returnType,
	                             final MethodInvocation methodInvocation) {
		return new AsyncResult<>(taskExecutor.submit(new Callable<AsyncResult<T>>() {
			@Override
			@SuppressWarnings("unchecked")
			public AsyncResult<T> call() throws Exception {
				AsyncResult<T> result;
				try {
					result = (AsyncResult<T>) methodInvocation.proceed();
				} catch (Throwable throwable) {
					throw new RuntimeException(throwable);
				}
				return result;
			}
		}));
	}

}
