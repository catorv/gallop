package com.catorv.gallop.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 对象反射
 * Created by cator on 6/21/16.
 */
public class Reflection {

	private static final String MODIFIERS_FIELD = "modifiers";

	public static List<Field> getDeclaredFields(Class<?> type) {
		List<Field> fields = new ArrayList<>();
		collectDeclaredFields(type, fields);
		return fields;
	}

	private static void collectDeclaredFields(Class<?> type, List<Field> fields) {
		if (type.getSuperclass() != null) {
			collectDeclaredFields(type.getSuperclass(), fields);
		}
		Collections.addAll(fields, type.getDeclaredFields());
	}

	public static void set(Field field, Object instance, Object value)
			throws IllegalAccessException, NoSuchFieldException, SecurityException {
		field.setAccessible(true);

		int modifiers = field.getModifiers();
		if (Modifier.isFinal(modifiers)) {
			setModifiers(field, modifiers & ~Modifier.FINAL);
			try {
				field.set(instance, value);
			} finally {
				setModifiers(field, modifiers | Modifier.FINAL);
			}
		} else {
			field.set(instance, value);
		}
	}

	public static void setModifiers(Field dst, int modifiers)
			throws IllegalAccessException, NoSuchFieldException, SecurityException {
		Field field = Field.class.getDeclaredField(MODIFIERS_FIELD);
		field.setAccessible(true);
		field.setInt(dst, modifiers);
	}

}