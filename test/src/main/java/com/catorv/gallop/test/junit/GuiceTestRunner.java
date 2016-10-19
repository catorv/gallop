package com.catorv.gallop.test.junit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Guice测试用例运行器
 * Created by cator on 6/20/16.
 */
public class GuiceTestRunner extends BlockJUnit4ClassRunner {

	private ExecutorService executorService;
	private List<Future<Object>> futures;
	private int numThreads;

	private ThreadLocal<Injector> injector = new ThreadLocal<>();

	public GuiceTestRunner(Class<?> clazz) throws InitializationError {
		super(clazz);

		if (clazz.isAnnotationPresent(MultiThreaded.class)) {
			MultiThreaded annotation = clazz.getAnnotation(MultiThreaded.class);
			executorService = new ThreadPoolExecutor(annotation.corePoolSize(),
					annotation.maximumPoolSize(), 300, TimeUnit.MINUTES,
					new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE));
			futures = new ArrayList<>();
			numThreads = annotation.numThreads();
			if (numThreads < 1) {
				throw new InitializationError("MultiThreaded.numThreads() less than 1.");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object createTest() throws Exception {
		List<Module> modules = new ArrayList<>();

		Class<?> targetClass = this.getTestClass().getJavaClass();
		if (targetClass.isAnnotationPresent(GuiceModule.class)) {
			for (Class<? extends Module> clazz : targetClass.getAnnotation(GuiceModule.class).value()) {
				modules.add(clazz.newInstance());
			}
		}
		if (Module.class.isAssignableFrom(targetClass)) {
			modules.add(((Class<? extends Module>) targetClass).newInstance());
		}
		Injector injector = this.injector.get();
		if (injector == null) {
			injector = Guice.createInjector(modules);
			this.injector.set(injector);
		}
		return injector.getInstance(targetClass);
	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		if (executorService == null) {
			super.runChild(method, notifier);
		} else {
			for (int i = 0; i < numThreads; i++) {
				Worker worker = new Worker(method, notifier);
				futures.add(executorService.submit(worker));
			}
		}
	}

	@Override
	protected Statement childrenInvoker(final RunNotifier notifier) {
		if (executorService == null) {
			return super.childrenInvoker(notifier);
		} else {
			return new Statement() {
				@Override
				public void evaluate() throws Throwable {
					GuiceTestRunner.super.childrenInvoker(notifier).evaluate();
					for (Future<Object> future : futures) {
						future.get();
					}
				}
			};
		}
	}

	class Worker implements Callable<Object> {

		private final FrameworkMethod method;
		private final RunNotifier notifier;

		public Worker(final FrameworkMethod method, final RunNotifier notifier) {
			this.method = method;
			this.notifier = notifier;
		}

		@Override
		public Object call() throws Exception {
			GuiceTestRunner.super.runChild(method, notifier);
			return null;
		}
	}

}