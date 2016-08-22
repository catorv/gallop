package com.catorv.gallop.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Inject Utils
 * Created by cator on 8/10/16.
 */
public class InjectUtils {

	@SafeVarargs
	public static AnnotatedMethods annotatedMethods(Class<?> clazz,
	                                                Class<? extends Annotation>... annotations) {
		AnnotatedMethods result = new AnnotatedMethods();
		if (annotations != null && annotations.length > 0) {
			while (clazz != null && clazz != Object.class) {
				for (Method method : clazz.getMethods()) {
					for (Class<? extends Annotation> annotation : annotations) {
						if (method.isAnnotationPresent(annotation)) {
							result.addMethod(method, annotation);
						}
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
		return result;
	}

}
