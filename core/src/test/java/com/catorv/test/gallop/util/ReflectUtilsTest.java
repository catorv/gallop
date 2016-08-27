package com.catorv.test.gallop.util;

import com.catorv.gallop.util.ReflectUtils;
import com.google.inject.TypeLiteral;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by cator on 8/26/16.
 */
public class ReflectUtilsTest {

	@Test
	public void testGetTypeName() throws NoSuchFieldException {
		Type type1 = (new TypeLiteral<Map<String, Map<Integer, Date>>>() {}).getType();
		Type type2 = (new TypeLiteral<String>() {}).getType();
		Type type3 = (new TypeLiteral<Map<Integer, Object[]>>() {}).getType();
		Type type4 = Integer[].class;
		Type type5 = int.class;
		double[] doubles = new double[] {1};
		Type type6 = doubles.getClass();

		Assert.assertEquals("java.util.Map<java.lang.String, java.util.Map<java.lang.Integer, java.util.Date>>", ReflectUtils.getTypeName(type1));
		assertEquals("java.lang.String", ReflectUtils.getTypeName(type2));
		assertEquals("java.util.Map<java.lang.Integer, java.lang.Object[]>", ReflectUtils.getTypeName(type3));
		assertEquals("java.lang.Integer[]", ReflectUtils.getTypeName(type4));
		assertEquals("int", ReflectUtils.getTypeName(type5));
		assertEquals("double[]", ReflectUtils.getTypeName(type6));
	}

	@Test
	public void testGetDeclaredFields() {
		List<Field> fields = ReflectUtils.getDeclaredFields(ClassB.class);
		assertEquals(6, fields.size());
	}

	@Test
	public void testGetDeclaredField() {
		assertNotNull(ReflectUtils.getDeclaredField(ClassA.class, "aIntField"));
		assertNotNull(ReflectUtils.getDeclaredField(ClassB.class, "aIntField"));
		assertNotNull(ReflectUtils.getDeclaredField(ClassB.class, "bIntField"));
		assertNull(ReflectUtils.getDeclaredField(ClassB.class, "cIntField"));
	}

	@Test
	public void testCopyValueOfFields1() throws IllegalAccessException {
		ClassB object1 = getTestObject();
		ClassB object2 = new ClassB();

		ReflectUtils.copy(object1, object2);

		assertNotNull(object2.getaIntField());
		assertEquals(object1.getaIntField(), object2.getaIntField());

		assertNotNull(object2.getaStringField());
		assertEquals(object1.getaStringField(), object2.getaStringField());

		assertNotNull(object2.getaMapField());
		assertEquals(object1.getaMapField(), object2.getaMapField());

		assertNotNull(object2.getbIntField());
		assertEquals(object1.getbIntField(), object2.getbIntField());

		assertNotNull(object2.getbStringField());
		assertEquals(object1.getbStringField(), object2.getbStringField());

		assertNotNull(object2.getbMapField());
		assertEquals(object1.getbMapField(), object2.getbMapField());
	}

	@Test
	public void testCopyValueOfFields2() throws IllegalAccessException {
		ClassB object = getTestObject();
		ClassOther objectOther = new ClassOther();

		ReflectUtils.copy(object, objectOther);

		assertNotNull(objectOther.getaIntField());
		assertEquals(object.getaIntField(), objectOther.getaIntField());

		assertNotNull(objectOther.getaStringField());
		assertEquals(object.getaStringField(), objectOther.getaStringField());

		assertNull(objectOther.getaMapField());

		assertNotNull(objectOther.getbIntField());
		assertEquals(object.getbIntField(), objectOther.getbIntField());

		assertNotNull(objectOther.getbStringField());
		assertEquals(object.getbStringField(), objectOther.getbStringField());

		assertNotNull(objectOther.getbMapField());
		assertEquals(object.getbMapField(), objectOther.getbMapField());

		assertNull(objectOther.getcStringField());
	}

	private ClassB getTestObject() {
		ClassB object = new ClassB();
		object.setaIntField(1);
		object.setaStringField("2");
		object.setaMapField(new HashMap<String, Object>());
		object.getaMapField().put("test", "String");
		object.setbIntField(11);
		object.setbStringField("22");
		object.setbMapField(new HashMap<String, Map<Integer, Date>>());
		Map<Integer, Date> map = new HashMap<>();
		map.put(0, new Date());
		object.getbMapField().put("test", map);
		return object;
	}

	private class ClassA {
		private int aIntField;
		public String aStringField;
		private Map<String, Object> aMapField;

		public int getaIntField() {
			return aIntField;
		}

		public void setaIntField(int aIntField) {
			this.aIntField = aIntField;
		}

		public String getaStringField() {
			return aStringField;
		}

		public void setaStringField(String aStringField) {
			this.aStringField = aStringField;
		}

		public Map<String, Object> getaMapField() {
			return aMapField;
		}

		public void setaMapField(Map<String, Object> aMapField) {
			this.aMapField = aMapField;
		}
	}

	private class ClassB extends ClassA {
		private int bIntField;
		public String bStringField;
		private Map<String, Map<Integer, Date>> bMapField;

		public int getbIntField() {
			return bIntField;
		}

		public void setbIntField(int bIntField) {
			this.bIntField = bIntField;
		}

		public String getbStringField() {
			return bStringField;
		}

		public void setbStringField(String bStringField) {
			this.bStringField = bStringField;
		}

		public Map<String, Map<Integer, Date>> getbMapField() {
			return bMapField;
		}

		public void setbMapField(Map<String, Map<Integer, Date>> bMapField) {
			this.bMapField = bMapField;
		}
	}

	public class ClassOther {
		private int aIntField;
		public String aStringField;
		private List<Date> aMapField;
		private int bIntField;
		public String bStringField;
		private Map<String, Map<Integer, Object>> bMapField;
		private String cStringField;

		public int getaIntField() {
			return aIntField;
		}

		public void setaIntField(int aIntField) {
			this.aIntField = aIntField;
		}

		public String getaStringField() {
			return aStringField;
		}

		public void setaStringField(String aStringField) {
			this.aStringField = aStringField;
		}

		public List<Date> getaMapField() {
			return aMapField;
		}

		public void setaMapField(List<Date> aMapField) {
			this.aMapField = aMapField;
		}

		public int getbIntField() {
			return bIntField;
		}

		public void setbIntField(int bIntField) {
			this.bIntField = bIntField;
		}

		public String getbStringField() {
			return bStringField;
		}

		public void setbStringField(String bStringField) {
			this.bStringField = bStringField;
		}

		public Map<String, Map<Integer, Object>> getbMapField() {
			return bMapField;
		}

		public void setbMapField(Map<String, Map<Integer, Object>> bMapField) {
			this.bMapField = bMapField;
		}

		public String getcStringField() {
			return cStringField;
		}

		public void setcStringField(String cStringField) {
			this.cStringField = cStringField;
		}
	}

}
