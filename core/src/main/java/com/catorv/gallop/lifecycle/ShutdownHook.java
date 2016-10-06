package com.catorv.gallop.lifecycle;

import com.catorv.gallop.log.LoggerFactory;
import com.google.inject.Singleton;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by cator on 6/21/16.
 */
@Singleton
public class ShutdownHook {

	private final static List<MethodInvocation> methodInvokes = new ArrayList<>();

	public synchronized static void register(MethodInvocation mi) {
		methodInvokes.add(mi);
	}

	@Initialize
	public void init() {
		registerShutdownHook();
	}

	private synchronized static void shutdown() {
		for (MethodInvocation mi : methodInvokes) {
			callMethod(mi);
		}
		methodInvokes.clear();
	}

	public synchronized static void callDestroyMethod(Object object) {
		List<MethodInvocation> mis = new ArrayList<>();
		for (MethodInvocation mi : methodInvokes) {
			if (mi.getThis() == object) {
				callMethod(mi);
				mis.add(mi);
			}
		}
		methodInvokes.removeAll(mis);
	}

	private synchronized static void callMethod(MethodInvocation mi) {
		try {
			Logger logger = new LoggerFactory().getLogger(ShutdownHook.class);
			logger.info("call method: {}", mi.getMethod());
			mi.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				shutdown();
			}
		});
	}
}
