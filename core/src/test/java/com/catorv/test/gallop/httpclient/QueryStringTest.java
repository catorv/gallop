package com.catorv.test.gallop.httpclient;

import com.catorv.gallop.httpclient.QueryString;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * QueryString Test
 * Created by cator on 8/15/16.
 */
public class QueryStringTest {

	@Test
	public void test() {
		QueryString queryString = new QueryString();
		queryString.put("aaa", true);
		queryString.put("bbb", 1);
		queryString.put("ccc", "haha");

		Assert.assertEquals("aaa=true&ccc=haha&bbb=1", queryString.toString());
	}

	@Test
	public void testFromMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("aaa", true);
		map.put("bbb", 1);
		map.put("ccc", "中文");

		QueryString queryString = new QueryString(map);

		Assert.assertEquals("aaa=true&ccc=%E4%B8%AD%E6%96%87&bbb=1", queryString.toString());
	}

	@Test
	public void testGBK() {
		QueryString queryString = new QueryString(Charset.forName("GBK"));
		queryString.put("aaa", true);
		queryString.put("bbb", 1);
		queryString.put("ccc", "中文");

		Assert.assertEquals("aaa=true&ccc=%D6%D0%CE%C4&bbb=1", queryString.toString());
	}

}
