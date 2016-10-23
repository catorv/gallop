package com.catorv.test.gallop.dataformat;

import com.catorv.gallop.dataformat.Xml;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.catorv.test.gallop.dataformat.TestData.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by cator on 9/3/16.
 */
public class XmlTest {

	@Test
	public void testReader() throws Exception {
		JsonBean bean = Xml.reader(JsonBean.class)
				.readValue(getBeanXmlString());

		assertEquals(getJsonBean(), bean);
	}

	@Test
	public void testWriter() throws Exception {
		String string = Xml.writer()
				.writeValueAsString(getJsonBean());

		assertEquals(getBeanXmlString(), string);
	}

	@Test
	public void testToString() throws Exception {
		String string = Xml.string(getJsonBean());
		assertEquals(getBeanXmlString(), string);

		String objectString = Xml.of(getJsonBean()).toString();
		assertEquals(getBeanXmlString(), objectString);

		String mapString = Xml.of(getMap()).toString();
		assertEquals(getMapXmlString(), mapString);

		String listString = Xml.of(getMap().get("citys")).toString();
		assertEquals(getCitiesXmlString(), listString);
	}

	@Test
	public void toByteArray() throws Exception {
		byte[] bytes = Xml.of(getJsonBean()).toBytes();
		assertArrayEquals(getBeanXmlString().getBytes(), bytes);
	}

	@Test
	public void testToObject() throws Exception {
		JsonBean jsonBean = Xml.of(getBeanXmlString()).to(JsonBean.class);
		assertEquals(getJsonBean(), jsonBean);

		JsonBean jsonBean2 = Xml.of(getBeanXmlString().getBytes())
				.to(JsonBean.class);
		assertEquals(getJsonBean(), jsonBean2);

		JavaType type = TypeFactory.defaultInstance().constructType(JsonBean.class);
		JsonBean jsonBean3 = Xml.of(getBeanXmlString()).to(type);
		assertEquals(getJsonBean(), jsonBean3);
	}

	@Test
	public void testToMap() throws Exception {
		Map<String, Object> map = Xml.of(getBeanXmlString()).toMap();
		assertEquals("123", map.get("integer"));
		assertEquals("s1", map.get("string"));

		Map map1 = Xml.of(getMapXmlString2()).toMap();
		assertEquals(2, map1.size());
		assertTrue(map1.get("obj1") instanceof Map);
		assertEquals("123", ((Map) map1.get("obj1")).get("integer"));
		assertEquals("223", ((Map) map1.get("obj2")).get("integer"));

		Map map2 = Xml.of(getMapXmlString2()).toMap(String.class, JsonBean.class);
		assertEquals(2, map1.size());
		assertTrue(map2.get("obj1") instanceof JsonBean);
		assertEquals(123, ((JsonBean) map2.get("obj1")).getInteger().intValue());
		assertEquals(223, ((JsonBean) map2.get("obj2")).getInteger().intValue());
	}

	@Test
	public void testToList() throws Exception {
		List cities = Xml.of(getCitiesXmlString()).toList();
		assertEquals(3, cities.size());
		assertTrue(cities.get(0) instanceof String);

		List beanList = Xml.of(getBeansXmlString()).toList();
		assertEquals(2, beanList.size());
		assertTrue(beanList.get(0) instanceof Map);
		assertEquals("123", ((Map) beanList.get(0)).get("integer"));
		assertEquals("223", ((Map) beanList.get(1)).get("integer"));

		List beanList2 = Xml.of(getBeansXmlString()).toList(JsonBean.class);
		assertEquals(2, beanList2.size());
		assertTrue(beanList2.get(0) instanceof JsonBean);
		assertEquals(123, ((JsonBean) beanList2.get(0)).getInteger().intValue());
		assertEquals(223, ((JsonBean) beanList2.get(1)).getInteger().intValue());
	}

	@Test
	public void testToArray() throws Exception {
		String[] strings = Xml.of(getCitiesXmlString()).toArray(String.class);
		assertEquals(3, strings.length);

		Object[] objects = Xml.of(getCitiesXmlString()).toArray();
		assertEquals(3, objects.length);
		assertEquals(String.class, objects[0].getClass());

		JsonBean[] array = Xml.of(getBeansXmlString()).toArray(JsonBean.class);
		assertEquals(2, array.length);
		assertEquals(123, array[0].getInteger().intValue());
		assertEquals(223, array[1].getInteger().intValue());
	}
}
