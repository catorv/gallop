package com.catorv.gallop.cfg;

import com.catorv.gallop.inject.AbstractNamedModule;
import com.catorv.gallop.inject.Namespace;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

import java.io.*;
import java.net.URL;
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
		Properties properties = readProperties();

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

	private Properties readProperties() {
		Properties properties = new Properties();
		InputStream is = null;
		InputStream is2 = null;

		try {
			URL url;
			String configFile = System.getProperty("config");

			File file = new File(Strings.isNullOrEmpty(configFile) ? "" : configFile);

			if (file.exists()) {
				url = file.toURI().toURL();
			} else {
				url = this.getClass().getClassLoader().getResource("config.properties");
			}
			assert url != null;

			System.out.println("[INFO] load config.properties from " + url.getFile() + " for " + name);
			is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			properties.load(isr);

			if (name != null) {
				String filename = url.getFile();
				String extname = Files.getFileExtension(filename);

				if (Strings.isNullOrEmpty(extname)) {
					filename = filename + "-" + name;
				} else {
					String basename = filename.substring(0, filename.length() - extname.length() - 1);
					filename = basename + "-" + name + "." + extname;
				}

				file = new File(filename);
				if (file.exists()) {
					System.out.println("[INFO] load config.properties from " + filename + " for " + name);
					is2 = new FileInputStream(file);
					InputStreamReader isr2 = new InputStreamReader(is2, "UTF-8");
					Properties namedProperties = new Properties();
					namedProperties.load(isr2);
					for (Object key : namedProperties.keySet()) {
						properties.put(key, namedProperties.get(key));
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (is2 != null) {
					is2.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return properties;
	}
}