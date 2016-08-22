package com.catorv.gallop.inject;

/**
 * Namespace工具类
 * Created by cator on 8/3/16.
 */
public class Namespaces {
	private Namespaces() {
	}

	public static Namespace named(String name) {
		return new NamespaceImpl(name);
	}
}
