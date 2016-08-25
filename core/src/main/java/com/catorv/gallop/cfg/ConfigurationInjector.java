package com.catorv.gallop.cfg;

import com.google.inject.MembersInjector;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

/**
 * 配置项注入器
 * Created by cator on 6/21/16.
 */
class ConfigurationInjector<T> implements MembersInjector<T> {

	private Field field;

	private String section;

	private Properties properties;

	private Class<?> groupType;

	private ConfigurationBuilder builder = new ConfigurationBuilder();

	ConfigurationInjector(Field field, String section, Properties properties) {
		this.field = field;
		this.section = section;
		this.properties = properties;
	}

	ConfigurationInjector(Field field, String section, Properties properties, Class<?> groupType) {
		this.field = field;
		this.section = section;
		this.properties = properties;
		this.groupType = groupType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void injectMembers(T ins) {
		if (field == null) {
			builder.build((Class<T>) ins.getClass(), ins, section, properties);
		} else {
			field.setAccessible(true);
			try {
				Class<T> type = (Class<T>) field.getType();
				if (type.isArray()) {
					final Class<?> componentType = type.getComponentType();
					field.set(ins, builder.buildArray(componentType, section, properties));
				} else if (Map.class.isAssignableFrom(type)) {
					if (groupType == Object.class) {
						field.set(ins, builder.buildHashMap(section, properties));
					} else {
						field.set(ins, builder.buildGrouped(groupType, null, section, properties));
					}
				} else {
					field.set(ins, builder.build(type, null, section, properties));
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
