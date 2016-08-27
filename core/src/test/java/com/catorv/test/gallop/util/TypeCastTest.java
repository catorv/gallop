package com.catorv.test.gallop.util;

import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.catorv.gallop.util.TypeCast;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

/**
 *
 * Created by cator on 8/3/16.
 */
@RunWith(GuiceTestRunner.class)
public class TypeCastTest {

	@Test
	public void testStringToDouble() {
		double delta = 0.000001;
		Assert.assertEquals(TypeCast.doubleOf("1.23"), 1.23, delta);
		Assert.assertEquals(TypeCast.doubleOf("1.23a"), 0, delta);
		Assert.assertEquals(TypeCast.doubleOf("a1.24"), 0,  delta);
		Assert.assertEquals(TypeCast.doubleOf(null), 0,  delta);
	}

	@Test
	public void testStringToFloat() {
		float delta = (float) 0.000001;
		Assert.assertEquals(TypeCast.floatOf("1.23"), 1.23, delta);
		Assert.assertEquals(TypeCast.floatOf("1.23a"), 0, delta);
		Assert.assertEquals(TypeCast.floatOf("a1.24"), 0,  delta);
		Assert.assertEquals(TypeCast.floatOf(null), 0,  delta);
	}

	@Test
	public void testStringToLong() {
		Assert.assertEquals(TypeCast.longOf("123456"), 123456);
		Assert.assertEquals(TypeCast.longOf("123456L"), 0);
		Assert.assertEquals(TypeCast.longOf("123.456"), 0);
		Assert.assertEquals(TypeCast.longOf("123456a"), 0);
		Assert.assertEquals(TypeCast.longOf("a123456"), 0);
		Assert.assertEquals(TypeCast.longOf(null), 0);
	}

	@Test
	public void testStringToInt() {
		Assert.assertEquals(TypeCast.intOf("123456"), 123456);
		Assert.assertEquals(TypeCast.intOf("123456L"), 0);
		Assert.assertEquals(TypeCast.intOf("123.456"), 0);
		Assert.assertEquals(TypeCast.intOf("123456a"), 0);
		Assert.assertEquals(TypeCast.intOf("a123456"), 0);
		Assert.assertEquals(TypeCast.intOf(null), 0);
	}

	@Test
	public void testStringToBoolean() {
		Assert.assertTrue(TypeCast.booleanOf("true"));
		Assert.assertTrue(TypeCast.booleanOf("yes"));
		Assert.assertTrue(TypeCast.booleanOf("True"));
		Assert.assertTrue(TypeCast.booleanOf("Yes"));
		Assert.assertFalse(TypeCast.booleanOf("false"));
		Assert.assertFalse(TypeCast.booleanOf("no"));
		Assert.assertFalse(TypeCast.booleanOf("False"));
		Assert.assertFalse(TypeCast.booleanOf("No"));
		Assert.assertFalse(TypeCast.booleanOf(null));
	}

	@Test
	public void testStringToDate() {
		Date date = new Date(1467850150000L);
		Assert.assertEquals(TypeCast.dateOf("2016-7-7 8:9:10", "yyyy-MM-dd HH:mm:ss"), date);
		Assert.assertNull(TypeCast.dateOf("2016-7-7", "yyyy-MM-dd HH:mm:ss"));
		Assert.assertNull(TypeCast.dateOf(null, "yyyy-MM-dd HH:mm:ss"));
	}

	@Test
	public void testObjectToString() {
		Assert.assertEquals(TypeCast.stringOf(null), "null");
		Assert.assertEquals(TypeCast.stringOf(123.45), "123.45");
		Assert.assertEquals(TypeCast.stringOf(true), "true");
	}

	@Test
	public void testObjectOf() {
		Date date = new Date();
		String string = "String";
		String nullString = null;

		Assert.assertNull(TypeCast.objectOf(String.class, date));
		Assert.assertNull(TypeCast.objectOf(String.class, nullString));

		Assert.assertNotNull(TypeCast.objectOf(String.class, string));
		Assert.assertNotNull(TypeCast.objectOf(CharSequence.class, string));
		Assert.assertNotNull(TypeCast.objectOf(Date.class, date));
	}

}
