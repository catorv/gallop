package com.catorv.gallop.cfg;

import com.catorv.gallop.inject.AbstractNamedModule;
import com.catorv.gallop.inject.Namespace;
import com.google.common.base.Strings;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件解析模块
 * Created by cator on 6/21/16.
 */
public class ConfigurationModule extends AbstractNamedModule {

	private static final Map<String, Properties> propertiesMap = new HashMap<>();
	private static final String DEFAULT_KEY = "*";

	public ConfigurationModule() {
		this(null);
	}

	public ConfigurationModule(String name) {
		super(name);
	}

	public static Properties getProperties(String name) {
		if (Strings.isNullOrEmpty(name)) {
			name = DEFAULT_KEY;
		}
		Properties properties = propertiesMap.get(name);
		if (properties == null && !DEFAULT_KEY.equals(name)) {
			properties = propertiesMap.get(DEFAULT_KEY);
		}
		return properties;
	}

	@Override
	protected void configure() {
		Properties properties = new ConfigurationLoader().load(name);

		bindListener(Matchers.any(), new ConfigurationInjectListener(properties));
		if (name != null) {
			bindNamespaceAnnotated(Properties.class).toInstance(properties);
			propertiesMap.put(name, properties);
		} else {
			propertiesMap.put(DEFAULT_KEY, properties);
		}
		bind(Properties.class).toInstance(properties);
		Names.bindProperties(binder(), properties);
		Names.bindProperties(binder(), System.getProperties());

		bindConstant().annotatedWith(Namespace.class).to(name == null ? "" : name);
	}

}