package com.catorv.test.gallop.io;

import com.catorv.gallop.io.JsonLineReader;
import com.catorv.gallop.io.JsonLineWriter;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

/**
 * Created by cator on 12/10/2016.
 */
public class JsonLineTest {

	@Test
	public void test() throws Exception {
		File file = File.createTempFile(getClass().getName(), "test");

		OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file));
		try (JsonLineWriter writer = new JsonLineWriter(os)) {
			for (int i = 0; i < 10; i++) {
				Model model = new Model("test_" + i, i);
				writer.writeJson(model);
			}
		}

		InputStreamReader is = new InputStreamReader(new FileInputStream(file));
		try (JsonLineReader reader = new JsonLineReader(is)) {
			JsonNode node;
			int i = 0;
			while ((node = reader.readJson()) != null) {
				Assert.assertEquals("test_" + i, node.get("name").asText());
				Assert.assertEquals(i, node.get("age").asInt());
				i++;
			}
		}

		InputStreamReader is2 = new InputStreamReader(new FileInputStream(file));
		try (JsonLineReader reader = new JsonLineReader(is2)) {
			Model model;
			int i = 0;
			while ((model = reader.readJson(Model.class)) != null) {
				Assert.assertEquals("test_" + i, model.getName());
				Assert.assertEquals(i, model.getAge());
				i++;
			}
		}

		file.delete();
	}

	public static class Model {

		private String name;
		private int age;

		public Model() {
		}

		public Model(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}

}
