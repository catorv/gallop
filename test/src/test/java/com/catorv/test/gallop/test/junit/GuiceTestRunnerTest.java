package com.catorv.test.gallop.test.junit;

import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * 含外部模块的测试用例
 * Created by cator on 6/20/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule(SampleModule.class)
public class GuiceTestRunnerTest extends AbstractModule {
	
	@Inject
	@Named("field")
	private String testField;
	
	@Inject
	@Named("field2")
	private String testField2;

	@Override
	protected void configure() {
		Map<String, String> properties = new HashMap<>();
		properties.put("field", "abc");
		Names.bindProperties(this.binder(), properties);
	}

	@Test
	public void testApp() {
		Assert.assertEquals("abc", testField);
		Assert.assertEquals("xyz", testField2);
	}
}
