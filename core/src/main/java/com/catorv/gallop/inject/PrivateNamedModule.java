package com.catorv.gallop.inject;

import com.google.inject.PrivateModule;
import com.google.inject.TypeLiteral;

/**
 * 可命名的私有Guice模块
 * Created by cator on 6/20/16.
 */
public abstract class PrivateNamedModule extends PrivateModule {

	protected String name;

	protected PrivateNamedModule(String name) {
		this.name = name;
	}

	public Namespace getNamespace() {
		return Namespaces.named(name == null ? "*" : name);
	}

	protected void exposeNamespaceAnnotated(Class<?> type) {
		expose(type).annotatedWith(getNamespace());
	}

	protected void exposeNamespaceAnnotated(TypeLiteral<?> type) {
		expose(type).annotatedWith(getNamespace());
	}

}
