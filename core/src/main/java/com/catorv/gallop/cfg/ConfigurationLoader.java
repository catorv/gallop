package com.catorv.gallop.cfg;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by cator on 8/25/16.
 */
class ConfigurationLoader {

	private static final String DEFAULT_FILENAME = "config.properties";

	private URL url;
	private Properties properties;

	ConfigurationLoader() {
		String configFile = System.getProperty("config");
		if (!Strings.isNullOrEmpty(configFile)) {
			File file = new File(configFile);
			if (file.exists()) {
				try {
					url = file.toURI().toURL();
				} catch (MalformedURLException ignored) {
					// nothing
				}
			}
		}

		if (url == null) {
			url = Resources.getResource(DEFAULT_FILENAME);
		}

		properties = new Properties();
	}

	Properties load(String name) {
		if (url != null) {
			loadConfig(null);

			if (name != null) {
				loadConfig(name);
			}

			if (name == null || !name.equals("test")) {
				loadConfig("test");
			}
		}

		return properties;
	}

	private void loadConfig(String name) {
		if (url == null) {
			return;
		}

		String filename = buildFilename(name);

		File file = new File(filename);
		if (file.exists()) {
			System.out.println("[INFO] load config from " + filename +
					" for " + (name == null ? "default" : name));

			Properties namedProperties = new Properties();
			try (InputStream is = new FileInputStream(file)) {
				final InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				namedProperties.load(isr);
				properties.putAll(namedProperties);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String buildFilename(String name) {
		String filename = url.getFile();

		if (name != null) {
			String extname = Files.getFileExtension(filename);
			if (Strings.isNullOrEmpty(extname)) {
				filename = filename + "-" + name;
			} else {
				final int nameLen = filename.length();
				final int extLen = extname.length();
				final String basename = filename.substring(0, nameLen - extLen - 1);
				filename = basename + "-" + name + "." + extname;
			}
		}

		return filename;
	}

}
