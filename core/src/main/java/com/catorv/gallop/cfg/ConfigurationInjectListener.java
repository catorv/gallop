package com.catorv.gallop.cfg;

import com.catorv.gallop.util.ReflectUtils;
import com.google.common.base.Strings;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * 配置文件注入监听器
 * Created by cator on 6/21/16.
 */
class ConfigurationInjectListener implements TypeListener {

	private Properties properties;

	ConfigurationInjectListener(Properties properties) {
		this.properties = properties;
	}

	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		Class<?> clazz = type.getRawType();
		while (clazz != null && clazz != Object.class) {
			if (clazz.isAnnotationPresent(Configuration.class)) {
				Configuration annotation = clazz.getAnnotation(Configuration.class);
				String section = getSection(annotation);
				encounter.register(new ConfigurationInjector<I>(null, section, properties));
			}
			for (Field field : ReflectUtils.getDeclaredFields(clazz)) {
				if (field.isAnnotationPresent(Configuration.class)) {
					Configuration annotation = field.getAnnotation(Configuration.class);
					String section = getSection(annotation);
					encounter.register(new ConfigurationInjector<I>(field, section, properties, annotation.groupType()));
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	private String getSection(Configuration annotation) {
		String section = annotation.value();
		if (Strings.isNullOrEmpty(section)) {
			return annotation.section();
		}
		return section;
	}
}