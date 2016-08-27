package com.catorv.test.gallop.json;

import com.catorv.gallop.json.JsonModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.BitSet;

/**
 *
 * Created by cator on 8/1/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule(JsonModule.class)
public class JsonTest {

	@Inject
	private ObjectMapper objectMapper;

	@Test
	public void test() throws IOException {
		JsonBean jsonBean = new JsonBean();
		jsonBean.setString("s1");
		jsonBean.setInteger(123);
		BitSet bs = new BitSet();
		bs.set(10020);
		jsonBean.setBitSet(bs);

		JsonBean jsonBean2 = objectMapper.readValue(
				objectMapper.writeValueAsString(jsonBean), JsonBean.class);

		Assert.assertEquals(jsonBean, jsonBean2);
	}
}

