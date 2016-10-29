package com.catorv.gallop.dataformat;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.BitSet;

/**
 * Created by cator on 29/10/2016.
 */
public class Csv {

	public static final CsvMapper objectMapper;

	static {
		objectMapper = new CsvMapper();
		SimpleModule module = new SimpleModule("CsvModule", new Version(1, 0,
				0, null, null, null));
		module.addSerializer(new BitSetSerializer());
		module.addDeserializer(BitSet.class, new BitSetDeserializer());
		objectMapper.registerModule(module);
	}

	public static CsvSchema schema(Class<?> type) {
		return objectMapper.schemaFor(type);
	}

	public static CsvSchema schema(TypeReference<?> type) {
		return objectMapper.schemaFor(type);
	}

	public static CsvSchema schema(JavaType type) {
		return objectMapper.schemaFor(type);
	}

	public static ObjectReader reader(Class<?> type) {
		return objectMapper.readerFor(type);
	}

	public static ObjectReader reader(TypeReference<?> type) {
		return objectMapper.readerFor(type);
	}

	public static ObjectReader reader(JavaType type) {
		return objectMapper.readerFor(type);
	}

	public static ObjectWriter writer() {
		return objectMapper.writer();
	}

	public static ObjectWriter writer(CsvSchema schema) {
		return objectMapper.writer(schema);
	}

	public static SequenceWriter writer(CsvSchema schema, File file)
			throws IOException {
		return objectMapper.writer(schema).writeValues(file);
	}

	public static SequenceWriter writer(CsvSchema schema, OutputStream os)
			throws IOException {
		return objectMapper.writer(schema).writeValues(os);
	}

	public static SequenceWriter writer(CsvSchema schema, Writer writer)
			throws IOException {
		return objectMapper.writer(schema).writeValues(writer);
	}

	public static SequenceWriter writer(Class<?> type, File file)
			throws IOException {
		return objectMapper.writerFor(type).with(objectMapper.schemaFor(type))
				.writeValues(file);
	}

	public static SequenceWriter writer(TypeReference<?> type, OutputStream os)
			throws IOException {
		return objectMapper.writerFor(type).with(objectMapper.schemaFor(type))
				.writeValues(os);
	}

	public static SequenceWriter writer(JavaType type, Writer writer)
			throws IOException {
		return objectMapper.writerFor(type).with(objectMapper.schemaFor(type))
				.writeValues(writer);
	}

	public static SequenceWriter writer(Class<?> type, CsvSchema schema, File file)
			throws IOException {
		return objectMapper.writerFor(type).with(schema).writeValues(file);
	}

	public static SequenceWriter writer(TypeReference<?> type, CsvSchema schema, OutputStream os)
			throws IOException {
		return objectMapper.writerFor(type).with(schema).writeValues(os);
	}

	public static SequenceWriter writer(JavaType type, CsvSchema schema, Writer writer)
			throws IOException {
		return objectMapper.writerFor(type).with(schema).writeValues(writer);
	}

}
