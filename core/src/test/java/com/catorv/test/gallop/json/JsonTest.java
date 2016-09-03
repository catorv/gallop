package com.catorv.test.gallop.json;

import com.catorv.gallop.json.Json;
import org.junit.Test;

import static com.catorv.test.gallop.json.TestData.*;
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
	public void testToObject() throws Exception {
		JsonBean jsonBean = Json.of(getBeanString()).to(JsonBean.class);
		assertEquals(getJsonBean(), jsonBean);

		JsonBean jsonBean2 = Json.of(getBeanString().getBytes())
				.to(JsonBean.class);
		assertEquals(getJsonBean(), jsonBean2);
	}

}
