package com.catorv.gallop.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象反射
 * Created by cator on 6/21/16.
 */
public class ReflectUtils {

	private static final String MODIFIERS_FIELD = "modifiers";

	public static List<Field> getDeclaredFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		collectDeclaredFields(clazz, fields);
		return fields;
	}

	private static void collectDeclaredFields(Class<?> clazz, List<Field> fields) {
		if (clazz.getSuperclass() != null) {
			collectDeclaredFields(clazz.getSuperclass(), fields);
		}
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getName().indexOf('$') < 0) {
				fields.add(field);
			}
		}
	}

	public static Field getDeclaredField(Class<?> clazz, String name) {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getName().equals(name)) {
				return field;
			}
		}
		if (clazz.getSuperclass() != null) {
			return getDeclaredField(clazz.getSuperclass(), name);
		}
		return null;
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

	public static String getTypeName(Type type) {
		if (type instanceof Class) {
			return ((Class) type).getCanonicalName();
		}
		return type.toString();
	}

	public static void copy(Object src, Object dest)
			throws IllegalAccessException {
		Map<String, Field> srcFieldMap = new HashMap<>();
		for (Field field : getDeclaredFields(src.getClass())) {
			srcFieldMap.put(field.getName(), field);
		}

		for (Field destField : getDeclaredFields(dest.getClass())) {
			final String key = destField.getName();
			if (srcFieldMap.containsKey(key)) {
				Field srcField = srcFieldMap.get(key);
				if (destField.getType().isAssignableFrom(srcField.getType())) {
					srcField.setAccessible(true);
					destField.setAccessible(true);
					destField.set(dest, srcField.get(src));
				}
			}
		}
	}

}