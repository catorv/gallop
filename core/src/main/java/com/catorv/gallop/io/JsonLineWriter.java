package com.catorv.gallop.io;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by cator on 12/10/2016.
 */
public class JsonLineWriter extends BufferedWriter {

	private ObjectMapper objectMapper;

	public JsonLineWriter(Writer out) {
		super(out);
		objectMapper = new ObjectMapper();
	}

	public JsonLineWriter(Writer out, int sz) {
		super(out, sz);
		objectMapper = new ObjectMapper();
	}

	public JsonLineWriter(Writer out, ObjectMapper objectMapper) {
		super(out);
		this.objectMapper = objectMapper;
	}

	public JsonLineWriter(Writer out, int sz, ObjectMapper objectMapper) {
		super(out, sz);
		this.objectMapper = objectMapper;
	}

	public void writeJson(Object object) throws IOException {
		String string = objectMapper.writeValueAsString(object);
		this.write(string);
		this.write('\n');
	}

}
