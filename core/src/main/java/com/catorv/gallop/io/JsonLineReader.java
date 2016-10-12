package com.catorv.gallop.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by cator on 12/10/2016.
 */
public class JsonLineReader extends BufferedReader {

	private ObjectMapper objectMapper;

	public JsonLineReader(Reader in, int sz) {
		super(in, sz);
		objectMapper = new ObjectMapper();
	}

	public JsonLineReader(Reader in) {
		super(in);
		objectMapper = new ObjectMapper();
	}

	public JsonLineReader(Reader in, int sz, ObjectMapper objectMapper) {
		super(in, sz);
		this.objectMapper = objectMapper;
	}

	public JsonLineReader(Reader in, ObjectMapper objectMapper) {
		super(in);
		this.objectMapper = objectMapper;
	}

	public JsonNode readJson() throws IOException {
		String line = readLine();
		if (line == null) return null;
		return objectMapper.readTree(line);
	}

	public <T> T readJson(Class<T> clazz) throws IOException {
		String line = readLine();
		if (line == null) return null;
		return objectMapper.readValue(line, clazz);
	}

}
