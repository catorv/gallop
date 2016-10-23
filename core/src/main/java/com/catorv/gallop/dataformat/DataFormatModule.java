package com.catorv.gallop.dataformat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * JSON Module
 * Created by cator on 8/1/16.
 */
public class DataFormatModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ObjectMapper.class).toInstance(Json.objectMapper);
		bind(XmlMapper.class).toInstance(Xml.xmlMapper);
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

