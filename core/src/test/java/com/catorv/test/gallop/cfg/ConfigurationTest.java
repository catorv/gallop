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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * 配置内容注入测试用例
 * Created by cator on 6/21/16.
 */
@RunWith(GuiceTestRunner.class)
public class ConfigurationTest extends AbstractModule {

	@Inject
	private ConfigurationTestModel1 testModel1;

	@Configuration("test.config")
	private ConfigurationTestModel2 testModel2;

	@Configuration("test.config.arrayValue1")
	private Integer[] integers;

	@Configuration("test.config.arrayValue2")
	private String[] strings;

	@Configuration("test.config.hash")
	private Map<String, String> map;

	@Configuration(section = "test.config.grouped", groupType = ConfigurationTestModel2.class)
	private Map<String, ConfigurationTestModel2> groups;

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
		Assert.assertEquals(namespace, "test");
		Assert.assertEquals(namespace2, "test1");
	}

	@Test
	public void testAnnotatedOnClass() {
		Assert.assertEquals(124, testModel1.intValue);
		Assert.assertEquals("12.3", String.valueOf(testModel1.floatValue));
		Assert.assertEquals("12.34", String.valueOf(testModel1.doubleValue));
		Assert.assertEquals(123123, testModel1.longValue);
		Assert.assertEquals("sssssss", testModel1.stringValue);
		Assert.assertEquals(new Date(1466481662000L), testModel1.dateValue);
		Assert.assertTrue(testModel1.booleanValue);
	}

	@Test
	public void testAnnotatedOnMethod() {
		Assert.assertEquals(new Date(1466481662000L), testModel2.dateValue);
		Assert.assertEquals(124, testModel2.intValue);
		Assert.assertEquals("12.3", String.valueOf(testModel2.floatValue));
		Assert.assertEquals("12.34", String.valueOf(testModel2.doubleValue));
		Assert.assertEquals(123123, testModel2.longValue);
		Assert.assertEquals("sssssss", testModel2.stringValue);
		Assert.assertEquals(3, testModel2.strings.length);
		Assert.assertArrayEquals(new String[]{"123", "abc", "wyz"}, testModel2.strings);
		Assert.assertTrue(testModel2.booleanValue);
	}

	@Test
	public void testSingleValue() {
		Assert.assertEquals("single value", singleValue);
	}

	@Test
	public void testProperties() {
		Assert.assertEquals("single value", properties.getProperty("test.config.singleValue"));
	}

	@Test
	public void testArrayConfiguration() {
		Assert.assertEquals(3, integers.length);
		Assert.assertArrayEquals(new Integer[]{123, 456, 789}, integers);

		Assert.assertEquals(3, strings.length);
		Assert.assertArrayEquals(new String[]{"abc", "efg", "hij"}, strings);
	}

	@Test
	public void testHashMapConfiguration() {
		Assert.assertEquals("key 1", map.get("key1"));
		Assert.assertEquals("key 2", map.get("key2"));
		Assert.assertEquals("key 3", map.get("key3"));
	}

	@Test
	public void testGroupedConfiguration() {
		Assert.assertTrue(groups.containsKey("group1"));
		Assert.assertTrue(groups.containsKey("group2"));

		ConfigurationTestModel2 testModel2 = groups.get("group1");
		Assert.assertEquals(new Date(1466481662000L), testModel2.dateValue);
		Assert.assertEquals(123, testModel2.intValue);
		Assert.assertEquals("12.3", String.valueOf(testModel2.floatValue));
		Assert.assertEquals("12.34", String.valueOf(testModel2.doubleValue));
		Assert.assertEquals(123123, testModel2.longValue);
		Assert.assertEquals("sssssss", testModel2.stringValue);
		Assert.assertEquals(3, testModel2.strings.length);
		Assert.assertArrayEquals(new String[]{"123", "abc", "wyz"}, testModel2.strings);
		Assert.assertTrue(testModel2.booleanValue);
	}

	@Test
	public void testSystemProperties() {
		Assert.assertFalse(Strings.isNullOrEmpty(userHome));
	}

}
