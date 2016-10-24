package com.catorv.gallop.test.junit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Guice测试用例运行器
 * Created by cator on 6/20/16.
 */
public class GuiceTestRunner extends BlockJUnit4ClassRunner {

	private ThreadLocal<Injector> injector = new ThreadLocal<>();

	public GuiceTestRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
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
		Class<?> targetClass = this.getTestClass().getJavaClass();

		if (targetClass.isAnnotationPresent(MultiThreaded.class)) {
			MultiThreaded annotation = targetClass.getAnnotation(MultiThreaded.class);

			int numThreads = annotation.numThreads();
			if (numThreads < 1) numThreads = 1;

			Worker[] workers = new Worker[numThreads];
			for (int i = 0; i < numThreads; i++) {
				workers[i] = new Worker(method, notifier);
			}

			try {
				runWorkers(workers, annotation.corePoolSize(), annotation.maximumPoolSize());
			} catch (InterruptedException | ExecutionException e) {
				Description description = Description.createTestDescription(targetClass, "");
				notifier.fireTestFailure(new Failure(description, e));
			}
		} else {
			super.runChild(method, notifier);
		}
	}

	private void runWorkers(Worker[] workers, int corePoolSize, int maximumPoolSize)
			throws InterruptedException, ExecutionException {
		ExecutorService executorService = new ThreadPoolExecutor(
				corePoolSize, maximumPoolSize, 300, TimeUnit.MINUTES,
				new LinkedBlockingQueue<Runnable>(workers.length));

		List<Future<Object>> futures = new ArrayList<>();
		for (Worker worker : workers) {
			futures.add(executorService.submit(worker));
		}

		for (Future<Object> future : futures) {
			future.get();
		}
	}

	private class Worker implements Callable<Object> {

		private final FrameworkMethod method;
		private final RunNotifier notifier;

		Worker(final FrameworkMethod method, final RunNotifier notifier) {
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