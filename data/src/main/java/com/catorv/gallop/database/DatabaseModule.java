package com.catorv.gallop.database;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.database.interceptor.*;
import com.catorv.gallop.database.jdbc.DataSourceProvider;
import com.catorv.gallop.database.jpa.HibernatePersistenceProviderResolver;
import com.catorv.gallop.inject.AbstractNamedModule;
import com.google.common.base.Strings;
import com.google.common.io.Resources;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.Properties;


/**
 * Database Module
 * 依赖模块:
 *  ConfigurationModule
 *  LifecycleModule
 * Created by cator on 8/11/16.
 */
public class DatabaseModule extends AbstractNamedModule {

	private String jpaUnit;

	public DatabaseModule() {
		this(null);
	}

	public DatabaseModule(String name) {
		super(name);
		jpaUnit = name == null ? "default" : name;
	}

	@Override
	protected void configure() {
		bind(DataSourceProvider.class);
		bind(DataSource.class).toProvider(DataSourceProvider.class);

		PersistenceProviderResolver ppr = new HibernatePersistenceProviderResolver();
		requestInjection(ppr);
		PersistenceProviderResolverHolder.setPersistenceProviderResolver(ppr);

		// JPA Persist
		Properties properties = buildProperties(jpaUnit);
		Properties configProperties = ConfigurationModule.getProperties(name);
		if (configProperties != null) {
			properties.putAll(configProperties);
		}
		install(new JpaPersistModule(jpaUnit).properties(properties));
		this.bind(JpaInitializer.class).asEagerSingleton();

		// Query
		QueryMethodInterceptor interceptor = new QueryMethodInterceptor();
		requestInjection(interceptor);
		Matcher<Class> matcher = Matchers.subclassesOf(EntityDAO.class);
		bindInterceptor(matcher, Matchers.annotatedWith(Select.class), interceptor);
		bindInterceptor(matcher, Matchers.annotatedWith(Count.class), interceptor);
		bindInterceptor(matcher, Matchers.annotatedWith(Delete.class), interceptor);
		bindInterceptor(matcher, Matchers.annotatedWith(Update.class), interceptor);
	}

	private Properties buildProperties(String jpaUnit) {
		Properties properties = new Properties();
		try {
			URL url = Resources.getResource("META-INF/persistence.xml");
			File file = new File(url.getFile());
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(file);
			NodeList units = document.getElementsByTagName("persistence-unit");
			for (int i = 0; i < units.getLength(); i++) {
				Node node = units.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element unit = (Element) node;
					String name = unit.getAttribute("name");
					if (jpaUnit.equals(name)) {
						NodeList propertiesNodes = unit.getElementsByTagName("properties");
						for (int j = 0; j < propertiesNodes.getLength(); j++) {
							Node propertiesNode = propertiesNodes.item(j);
							if (propertiesNode.getNodeType() == Node.ELEMENT_NODE) {
								Element propertiesElement = (Element) propertiesNode;
								NodeList propertyNodes = propertiesElement.getElementsByTagName("property");
								for (int k = 0; k < propertyNodes.getLength(); k++) {
									Node propertyNode = propertyNodes.item(k);
									if (propertiesNode.getNodeType() == Node.ELEMENT_NODE) {
										Element property = (Element) propertyNode;
										String propertyName = property.getAttribute("name");
										String propertyValue = property.getAttribute("value");
										if (Strings.isNullOrEmpty(propertyName)
												|| Strings.isNullOrEmpty(propertyName)) {
											continue;
										}
										properties.setProperty(propertyName, propertyValue);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// nothing
		}

		return properties;
	}
}
