package com.catorv.gallop;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.inject.AbstractNamedModule;
import com.catorv.gallop.json.JsonModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.log.LoggerModule;
import com.catorv.gallop.observable.ObservableModule;

/**
 * Created by cator on 8/22/16.
 */
public class CoreModule extends AbstractNamedModule {

	public CoreModule() {
		this(null);
	}

	public CoreModule(String name) {
		super(name);
	}

	@Override
	protected void configure() {
		install(new ConfigurationModule(name));
		install(new LifecycleModule());
		install(new LoggerModule());
		install(new ObservableModule());
		install(new JsonModule());
	}
}
