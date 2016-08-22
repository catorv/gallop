package com.catorv.gallop.cfg;

import com.catorv.gallop.util.Reflection;
import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 配置参数
 * Created by cator on 6/21/16.
 */
@Singleton
public class ConfigurationBuilder {

	private ConfigurationValueConverter converter = new ConfigurationValueConverter();

	public <T> T buildConfiguration(Class<T> resultType, T result, String section,
	                         Properties properties) {
		try {
			result = (result == null ? resultType.newInstance() : result);
			for (Field field : Reflection.getDeclaredFields(resultType)) {
				Class<?> type = field.getType();

				if (Modifier.isFinal(field.getModifiers())) {
					continue;
				}

				if (type.isArray()) {
					Object value = buildArrayConfiguration(type.getComponentType(),
							section + "." + getFieldName(field), properties);
					field.setAccessible(true);
					field.set(result, value);
					continue;
				}

				if (!converter.isSupport(String.class, type)) {
					continue;
				}

				field.setAccessible(true);
				String stringValue = properties.getProperty(section + "." + getFieldName(field));
				Object value = converter.fromString(stringValue , type);
				if (value == null) {
					continue;
				}
				field.set(result, value);
			}
			return result;
		} catch (IllegalAccessException | InstantiationException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T[] buildArrayConfiguration(Class<T> componentType,
	                                String section, Properties properties) {
		String stringValue = properties.getProperty(section);
		if (Strings.isNullOrEmpty(stringValue)) {
			return null;
		}

		String[] parts = stringValue.split("\\|");

		if (!converter.isSupport(String.class, componentType)) {
			return null;
		}

		T[] result = (T[]) Array.newInstance(componentType, parts.length);
		for (int i = 0; i < parts.length; i++) {
			result[i] = (T) converter.fromString(parts[i], componentType);
		}
		return result;
	}

	public Map<String, String> buildHashMapConfiguration(String section, Properties properties) {
		Map<String, String> result = new HashMap<>();
		section = section + ".";
		Set<Object> keys = properties.keySet();
		for (Object key : keys) {
			String k = key.toString();
			if (k.startsWith(section)) {
				result.put(k.substring(section.length()), properties.getProperty(k));
			}
		}
		return result;
	}

	public <T> Map<String, T> buildGroupedConfiguration(Class<T> groupType, Map<String, T> result,
	                                             String section, Properties properties) {
		if (result == null) {
			result = new HashMap<>();
		}

		section = section + ".";

		Set<String> groups = new HashSet<>();
		for (Object key : properties.keySet()) {
			String k = key.toString();
			if (k.startsWith(section)) {
				String subsection = k.substring(section.length());
				int pos = subsection.indexOf('.');
				if (pos > 0) {
					groups.add(subsection.substring(0, pos));
				}
			}
		}

		for (String group : groups) {
			result.put(group, buildConfiguration(groupType, null, section + group, properties));
		}

		return result;
	}

	private String getFieldName(Field field) {
		String fieldName = null;
		if (field.isAnnotationPresent(Named.class)) {
			fieldName = field.getAnnotation(Named.class).value();
		}
		return Strings.isNullOrEmpty(fieldName) ? field.getName() : fieldName;
	}
}
