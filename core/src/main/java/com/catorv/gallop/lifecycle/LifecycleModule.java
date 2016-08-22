package com.catorv.gallop.lifecycle;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * 对象生命周期管理模块
 * Created by cator on 6/21/16.
 */
public class LifecycleModule extends AbstractModule {

	@Override
	protected void configure() {
		bindListener(Matchers.any(), new LifecycleListener());
		bind(ShutdownHook.class).asEagerSingleton();
	}

}

