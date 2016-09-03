package com.catorv.test.gallop.json;

import java.util.*;

/**
 * Created by cator on 9/3/16.
 */
class TestData {

	static JsonBean getJsonBean() {
		JsonBean jsonBean = new JsonBean();
		jsonBean.setString("s1");
		jsonBean.setInteger(123);
		BitSet bs = new BitSet();
		bs.set(10020);
		jsonBean.setBitSet(bs);
		return jsonBean;
	}

	static String getBeanString() {
		return "{\"string\":\"s1\",\"integer\":123,\"bitSet\":\"0|0|0|0|0|0|0|0|" +
				"0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0" +
				"|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|" +
				"0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0" +
				"|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|" +
				"0|0|0|0|0|0|0|0|0|0|0|0|0|0|68719476736|\"}";
	}

	static Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "cator");
		map.put("age", 38);
		map.put("male", true);
		map.put("date", new Date(1472881643617L));
		map.put("pets", new String[]{"cat", "dog"});
		map.put("bean", getJsonBean());
		List<String> citys = new ArrayList<>();
		citys.add("上海");
		citys.add("福州");
		citys.add("重庆");
		map.put("citys", citys);
		return map;
	}

	static String getMapString() {
		return "{\"date\":1472881643617,\"pets\":[\"cat\",\"dog\"],\"name\":\"ca" +
				"tor\",\"citys\":[\"上海\",\"福州\",\"重庆\"],\"age\":38,\"male\":true" +
				",\"bean\":{\"string\":\"s1\",\"integer\":123,\"bitSet\":\"0|0|0|0|0" +
				"|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|" +
				"0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0" +
				"|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|" +
				"0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0" +
				"|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|68719476736|\"}}";
	}

	static String getCitiesString() {
		return "[\"上海\",\"福州\",\"重庆\"]";
	}

}
