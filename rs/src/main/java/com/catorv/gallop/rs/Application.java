package com.catorv.gallop.rs;

import com.catorv.gallop.rs.guice.bridge.api.GuiceBridge;
import com.catorv.gallop.rs.guice.bridge.api.GuiceIntoHK2Bridge;
import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by cator on 24/10/2016.
 */
public class Application extends ResourceConfig {

	public static void bridgeGuiceInjector(Injector injector,
	                                       ServiceLocator locator) {
		GuiceBridge.getGuiceBridge().initializeGuiceBridge(locator);
		GuiceIntoHK2Bridge g2h = locator.getService(GuiceIntoHK2Bridge.class);
		g2h.bridgeGuiceInjector(injector);
	}

}
