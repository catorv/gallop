package com.catorv.gallop.test.junit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;

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
}