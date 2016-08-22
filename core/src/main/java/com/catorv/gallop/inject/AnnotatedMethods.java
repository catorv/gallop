package com.catorv.gallop.inject;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

/**
 * Annotated Methods
 * Created by cator on 8/10/16.
 */
public class AnnotatedMethods {
	private Multimap<Class<? extends Annotation>, Method> methods;

	public AnnotatedMethods() {
		methods = ArrayListMultimap.create();
	}

	public void addMethod(Method method, Class<? extends Annotation> annotation) {
		methods.put(annotation, method);
	}

	public Set<Class<? extends Annotation>> getAnnotations() {
		return methods.keySet();
	}

	public Collection<Method> getMethods(Class<? extends Annotation> annotation) {
		return methods.get(annotation);
	}

}
