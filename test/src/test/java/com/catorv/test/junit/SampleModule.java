package com.catorv.test.junit;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.util.HashMap;
import java.util.Map;

/**
 * 例子模块(测试用例)
 * Created by cator on 6/20/16.
 */
public class SampleModule extends AbstractModule {

	@Override
	protected void configure() {
		Map<String, String> properties = new HashMap<>();
		properties.put("field2", "xyz");
		Names.bindProperties(this.binder(), properties);
	}

}