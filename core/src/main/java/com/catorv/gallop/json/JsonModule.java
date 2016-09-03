package com.catorv.gallop.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * JSON Module
 * Created by cator on 8/1/16.
 */
public class JsonModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(ObjectMapper.class).toInstance(Json.objectMapper);
	}

	@Provides
	public ObjectReader getObjectReader() {
		return Json.objectMapper.reader();
	}

	@Provides
	public ObjectWriter getObjectWriter() {
		return Json.objectMapper.writer();
	}

}

