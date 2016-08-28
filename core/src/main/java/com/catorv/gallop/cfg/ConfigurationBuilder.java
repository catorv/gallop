package com.catorv.gallop.cfg;

import com.catorv.gallop.util.ReflectUtils;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.lang.reflect.*;
import java.util.*;

/**
 * 配置参数
 * Created by cator on 6/21/16.
 */
@Singleton
public class ConfigurationBuilder {

	@Inject
	private Properties properties;

	private ConfigurationValueConverter converter = new ConfigurationValueConverter();

	public <T> T build(String section, Class<T> resultType) {
		if (properties == null) {
			throw new IllegalArgumentException("properties is null.");
		}
		return build(resultType, null, section, properties);
	}

	public <T> T build(Class<T> resultType, T result, String section,
	                   Properties properties) {
		try {
			result = (result == null ? resultType.newInstance() : result);
			for (Field field : ReflectUtils.getDeclaredFields(resultType)) {
				Class<?> type = field.getType();

				if (Modifier.isFinal(field.getModifiers())) {
					continue;
				}

				final String key = section + "." + getFieldName(field);

				if (type.isArray()) {
					Object value = buildArray(type.getComponentType(), key, properties);
					field.setAccessible(true);
					field.set(result, value);
					continue;
				}

				if (Map.class.isAssignableFrom(type)) {
					ParameterizedType mapType = (ParameterizedType) field.getGenericType();
					Class<?> groupType = (Class<?>) mapType.getActualTypeArguments()[1];
					field.setAccessible(true);
					if (groupType == Object.class) {
						field.set(result, buildHashMap(key, properties));
					} else {
						field.set(result, buildGrouped(groupType, null, key, properties));
					}
					continue;
				}

				if (!converter.isSupport(String.class, type)) {
					continue;
				}

				field.setAccessible(true);
				String stringValue = properties.getProperty(key);
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
	<T> T[] buildArray(Class<T> componentType, String section,
	                   Properties properties) {
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

	Map<String, String> buildHashMap(String section, Properties properties) {
		Map<String, String> result = new HashMap<>();
		section = section + ".";
		for (Object key : properties.keySet()) {
			String k = key.toString();
			if (k.startsWith(section)) {
				result.put(k.substring(section.length()), properties.getProperty(k));
			}
		}
		return result;
	}

	<T> Map<String, T> buildGrouped(Class<T> groupType, Map<String, T> result,
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
			result.put(group, build(groupType, null, section + group, properties));
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
