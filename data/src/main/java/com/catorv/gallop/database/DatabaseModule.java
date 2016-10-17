package com.catorv.gallop.database;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.database.interceptor.*;
import com.catorv.gallop.database.jdbc.DataSourceProvider;
import com.catorv.gallop.database.jpa.HibernatePersistenceProviderResolver;
import com.catorv.gallop.inject.AbstractNamedModule;
import com.catorv.gallop.log.LoggerFactory;
import com.google.common.base.Strings;
import com.google.common.io.Resources;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
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
		URL url = Resources.getResource("META-INF/persistence.xml");
		try (InputStream is = url.openStream()) {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(is);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/persistence/" +
					"persistence-unit[@name='" + jpaUnit + "']/" +
					"properties/property");

			NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				final Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					final Element property = (Element) node;
					final String name = property.getAttribute("name");
					final String value = property.getAttribute("value");
					if (Strings.isNullOrEmpty(name)
							|| Strings.isNullOrEmpty(name)) {
						continue;
					}
					properties.setProperty(name, value);
				}
			}

		} catch (IOException | ParserConfigurationException | SAXException |
				XPathExpressionException e) {
			Logger logger = new LoggerFactory().getLogger(getClass());
			logger.warn("failed to read persistence.xml", e);
		}

		return properties;
	}
}
