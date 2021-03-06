package com.catorv.test.gallop.dataformat;

import com.catorv.gallop.dataformat.DataFormatModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 *
 * Created by cator on 8/1/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule(DataFormatModule.class)
public class ObjectMapperTest {

	@Inject
	private ObjectMapper objectMapper;

	@Inject
	private ObjectWriter objectWriter;

	@Inject
	private ObjectReader objectReader;

	@Test
	public void test() throws IOException {
		JsonBean jsonBean = TestData.getJsonBean();

		JsonBean jsonBean2 = objectMapper.readValue(
				objectMapper.writeValueAsString(jsonBean), JsonBean.class);

		Assert.assertEquals(jsonBean, jsonBean2);
	}

	@Test
	public void testReaderAndWriter() throws Exception {
		JsonBean jsonBean = TestData.getJsonBean();
		JsonBean jsonBean2 = objectReader.forType(JsonBean.class)
				.readValue(objectWriter.writeValueAsString(jsonBean));

		Assert.assertEquals(jsonBean, jsonBean2);

		System.out.println(objectWriter.writeValueAsString(jsonBean));
	}

}

