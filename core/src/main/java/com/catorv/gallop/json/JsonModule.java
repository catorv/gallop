package com.catorv.gallop.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.AbstractModule;

import java.util.BitSet;

/**
 * JSON Module
 * Created by cator on 8/1/16.
 */
public class JsonModule extends AbstractModule {

	@Override
	protected void configure() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		SimpleModule testModule = new SimpleModule("JsonModule", new Version(1, 0,
				0, null, null, null));
		testModule.addSerializer(new BitSetSerializer());
		testModule.addDeserializer(BitSet.class, new BitSetDeserializer());
		objectMapper.registerModule(testModule);

		this.bind(ObjectMapper.class).toInstance(objectMapper);
	}

}

