package com.catorv.gallop.cfg;

import com.catorv.gallop.log.LoggerFactory;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.slf4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Created by cator on 8/25/16.
 */
class ConfigurationLoader {

	private static final String DEFAULT_FILENAME = "config.properties";
	private static final String GLOBAL_NAME = "global";

	private URL url;
	private Properties properties;

	private Logger logger;

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

		properties = new Properties();

		logger = new LoggerFactory().getLogger(getClass());
	}

	Properties load(String name) {
		loadConfig(null);

		if (name != null) {
			loadConfig(name);
		}

		if (name == null || !name.equals(GLOBAL_NAME)) {
			loadConfig(GLOBAL_NAME);
		}

		return properties;
	}

	private void loadConfig(String name) {
		String filename = buildFilename(name);

		if (url != null) {
			File file = new File(filename);
			if (file.exists()) {
				logger.info("load config from {} for {}",
						filename, (name == null ? "default" : name));

				try (InputStream is = new FileInputStream(file)) {
					final InputStreamReader isr = new InputStreamReader(is,
							Charset.defaultCharset());
					Properties namedProperties = new Properties();
					namedProperties.load(isr);
					properties.putAll(namedProperties);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				URL resource = Resources.getResource(filename);
				logger.info("load config from {} for {}",
						resource.getFile(), (name == null ? "default" : name));
				InputStreamReader isr = new InputStreamReader(resource.openStream(),
						Charset.defaultCharset());
				Properties namedProperties = new Properties();
				namedProperties.load(isr);
				properties.putAll(namedProperties);
			} catch (Exception e) {
				// nothing
			}
		}
	}

	private String buildFilename(String name) {
		String filename = url != null ? url.getFile() : DEFAULT_FILENAME;

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
