package com.catorv.test.gallop.json;

import com.catorv.gallop.dataformat.Json;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.catorv.test.gallop.json.TestData.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by cator on 9/3/16.
 */
public class JsonTest {

	@Test
	public void testReader() throws Exception {
		JsonBean bean = Json.reader(JsonBean.class)
				.readValue(getBeanString());

		assertEquals(getJsonBean(), bean);
	}

	@Test
	public void testWriter() throws Exception {
		String string = Json.writer()
				.writeValueAsString(getJsonBean());

		assertEquals(getBeanString(), string);
	}

	@Test
	public void testToString() throws Exception {
		String string = Json.string(getJsonBean());
		assertEquals(getBeanString(), string);

		String objectString = Json.of(getJsonBean()).toString();
		assertEquals(getBeanString(), objectString);

		String mapString = Json.of(getMap()).toString();
		assertEquals(getMapString(), mapString);

		String listString = Json.of(getMap().get("citys")).toString();
		assertEquals(getCitiesString(), listString);
	}

	@Test
	public void toByteArray() throws Exception {
		byte[] bytes = Json.of(getJsonBean()).toBytes();
		assertArrayEquals(getBeanString().getBytes(), bytes);
	}

	@Test
	public void testToObject() throws Exception {
		JsonBean jsonBean = Json.of(getBeanString()).to(JsonBean.class);
		assertEquals(getJsonBean(), jsonBean);

		JsonBean jsonBean2 = Json.of(getBeanString().getBytes())
				.to(JsonBean.class);
		assertEquals(getJsonBean(), jsonBean2);

		JavaType type = TypeFactory.defaultInstance().constructType(JsonBean.class);
		JsonBean jsonBean3 = Json.of(getBeanString()).to(type);
		assertEquals(getJsonBean(), jsonBean3);
	}

	@Test
	public void testToMap() throws Exception {
		Map<String, Object> map = Json.of(getBeanString()).toMap();
		assertEquals(123, map.get("integer"));
		assertEquals("s1", map.get("string"));

		Map map1 = Json.of(getMapString2()).toMap();
		assertEquals(2, map1.size());
		assertTrue(map1.get("obj1") instanceof Map);
		assertEquals(123, ((Integer) ((Map) map1.get("obj1")).get("integer")).intValue());
		assertEquals(223, ((Integer) ((Map) map1.get("obj2")).get("integer")).intValue());

		Map map2 = Json.of(getMapString2()).toMap(String.class, JsonBean.class);
		assertEquals(2, map1.size());
		assertTrue(map2.get("obj1") instanceof JsonBean);
		assertEquals(123, ((JsonBean) map2.get("obj1")).getInteger().intValue());
		assertEquals(223, ((JsonBean) map2.get("obj2")).getInteger().intValue());
	}

	@Test
	public void testToList() throws Exception {
		List cities = Json.of(getCitiesString()).toList();
		assertEquals(3, cities.size());
		assertTrue(cities.get(0) instanceof String);

		List beanList = Json.of(getBeansString()).toList();
		assertEquals(2, beanList.size());
		assertTrue(beanList.get(0) instanceof Map);
		assertEquals(123, ((Integer) ((Map) beanList.get(0)).get("integer")).intValue());
		assertEquals(223, ((Integer) ((Map) beanList.get(1)).get("integer")).intValue());

		List beanList2 = Json.of(getBeansString()).toList(JsonBean.class);
		assertEquals(2, beanList2.size());
		assertTrue(beanList2.get(0) instanceof JsonBean);
		assertEquals(123, ((JsonBean) beanList2.get(0)).getInteger().intValue());
		assertEquals(223, ((JsonBean) beanList2.get(1)).getInteger().intValue());
	}

	@Test
	public void testToArray() throws Exception {
		String[] strings = Json.of(getCitiesString()).toArray(String.class);
		assertEquals(3, strings.length);

		Object[] objects = Json.of(getCitiesString()).toArray();
		assertEquals(3, objects.length);
		assertEquals(String.class, objects[0].getClass());
	}
}
