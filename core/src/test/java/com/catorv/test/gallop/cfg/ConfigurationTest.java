package com.catorv.test.gallop.cfg;

import com.catorv.gallop.cfg.Configuration;
import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.inject.Namespace;
import com.catorv.gallop.inject.Namespaces;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * 配置内容注入测试用例
 * Created by cator on 6/21/16.
 */
@RunWith(GuiceTestRunner.class)
public class ConfigurationTest extends AbstractModule {

	@Inject
	private Model1 testModel1;

	@Configuration("test.config")
	private Model2 testModel2;

	@Configuration("test.config.arrayValue1")
	private Integer[] integers;

	@Configuration("test.config.arrayValue2")
	private String[] strings;

	@Configuration("test.config.hash")
	private Map<String, String> map;

	@Configuration(section = "test.config.grouped", groupType = Model2.class)
	private Map<String, Model2> groups;

	@Inject
	private Model3 model3;

	@Inject
	@Named("test.config.singleValue")
	private String singleValue;

	@Inject
	private Properties properties;

	@Inject
	@Namespace
	private String namespace;

	@Inject
	@Namespace("test1")
	private String namespace2;

	@Inject
	@Named("user.home")
	private String userHome;

	@Override
	protected void configure() {
		install(new ConfigurationModule("test"));
		bindConstant().annotatedWith(Namespaces.named("test1")).to("test1");
	}

	@Test
	public void testNamespaceAnnotation() {
		assertEquals(namespace, "test");
		assertEquals(namespace2, "test1");
	}

	@Test
	public void testAnnotatedOnClass() {
		assertEquals(124, testModel1.intValue);
		assertEquals("12.3", String.valueOf(testModel1.floatValue));
		assertEquals("12.34", String.valueOf(testModel1.doubleValue));
		assertEquals(123123, testModel1.longValue);
		assertEquals("sssssss", testModel1.stringValue);
		assertEquals(new Date(1466481662000L), testModel1.dateValue);
		assertTrue(testModel1.booleanValue);
	}

	@Test
	public void testAnnotatedOnMethod() {
		assertEquals(new Date(1466481662000L), testModel2.dateValue);
		assertEquals(124, testModel2.intValue);
		assertEquals("12.3", String.valueOf(testModel2.floatValue));
		assertEquals("12.34", String.valueOf(testModel2.doubleValue));
		assertEquals(123123, testModel2.longValue);
		assertEquals("sssssss", testModel2.stringValue);
		assertEquals(3, testModel2.strings.length);
		assertArrayEquals(new String[]{"123", "abc", "wyz"}, testModel2.strings);
		assertTrue(testModel2.booleanValue);
	}

	@Test
	public void testSingleValue() {
		assertEquals("single value", singleValue);
	}

	@Test
	public void testProperties() {
		assertEquals("single value", properties.getProperty("test.config.singleValue"));
	}

	@Test
	public void testArrayConfiguration() {
		assertEquals(3, integers.length);
		assertArrayEquals(new Integer[]{123, 456, 789}, integers);

		assertEquals(3, strings.length);
		assertArrayEquals(new String[]{"abc", "efg", "hij"}, strings);
	}

	@Test
	public void testHashMapConfiguration() {
		assertEquals("key 1", map.get("key1"));
		assertEquals("key 2", map.get("key2"));
		assertEquals("key 3", map.get("key3"));
	}

	@Test
	public void testGroupedConfiguration() {
		assertTrue(groups.containsKey("group1"));
		assertTrue(groups.containsKey("group2"));

		Model2 testModel2 = groups.get("group1");
		assertEquals(new Date(1466481662000L), testModel2.dateValue);
		assertEquals(123, testModel2.intValue);
		assertEquals("12.3", String.valueOf(testModel2.floatValue));
		assertEquals("12.34", String.valueOf(testModel2.doubleValue));
		assertEquals(123123, testModel2.longValue);
		assertEquals("sssssss", testModel2.stringValue);
		assertEquals(3, testModel2.strings.length);
		assertArrayEquals(new String[]{"123", "abc", "wyz"}, testModel2.strings);
		assertTrue(testModel2.booleanValue);
	}

	@Test
	public void testSystemProperties() {
		assertFalse(Strings.isNullOrEmpty(userHome));
	}

	@Test
	public void testGroupedProperties() throws Exception {
		Map<String, Model2> groups = model3.getGroups();

		assertTrue(groups.containsKey("group1"));
		assertTrue(groups.containsKey("group2"));

		Model2 testModel2 = groups.get("group1");
		assertEquals(new Date(1466481662000L), testModel2.dateValue);
		assertEquals(123, testModel2.intValue);
		assertEquals("12.3", String.valueOf(testModel2.floatValue));
		assertEquals("12.34", String.valueOf(testModel2.doubleValue));
		assertEquals(123123, testModel2.longValue);
		assertEquals("sssssss", testModel2.stringValue);
		assertEquals(3, testModel2.strings.length);
		assertArrayEquals(new String[]{"123", "abc", "wyz"}, testModel2.strings);
		assertTrue(testModel2.booleanValue);
	}
}
