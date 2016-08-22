package com.catorv.gallop.inject;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.LinkedBindingBuilder;

/**
 * 可命名的Guice模块
 * Created by cator on 6/20/16.
 */
public abstract class AbstractNamedModule extends AbstractModule {

	protected String name;

	protected AbstractNamedModule(String name) {
		this.name = name;
	}

	public Namespace getNamespace() {
		return Namespaces.named(name == null ? "*" : name);
	}

	protected <T> LinkedBindingBuilder<T> bindNamespaceAnnotated(Class<T> clazz) {
		return bind(clazz).annotatedWith(getNamespace());
	}

	protected <T> LinkedBindingBuilder<T> bindNamespaceAnnotated(TypeLiteral<T> typeLiteral) {
		return bind(typeLiteral).annotatedWith(getNamespace());
	}
}