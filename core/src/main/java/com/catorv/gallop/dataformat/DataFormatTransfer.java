package com.catorv.gallop.dataformat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cator on 23/10/2016.
 */
public class DataFormatTransfer {

	private Object object;

	DataFormatTransfer(Object object) {
		this.object = object;
	}

	public String toString() {
		return Json.string(object);
	}

	public byte[] toBytes() throws JsonProcessingException {
		return Json.objectMapper.writer().writeValueAsBytes(object);
	}

	public DataFormatTransfer to(File file) throws IOException {
		Json.objectMapper.writer().writeValue(file, object);
		return this;
	}

	public DataFormatTransfer to(OutputStream outputStream) throws IOException {
		Json.objectMapper.writer().writeValue(outputStream, object);
		return this;
	}

	public DataFormatTransfer to(Writer writer) throws IOException {
		Json.objectMapper.writer().writeValue(writer, object);
		return this;
	}

	public <T> T to(Class<T> clazz) throws IOException {
		return read(Json.objectMapper.readerFor(clazz));
	}

	public <T> T to(JavaType type) throws IOException {
		return read(Json.objectMapper.readerFor(type));
	}

	public <K, V> Map<K, V> toMap(Class<K> keyClass, Class<V> valueClass)
			throws IOException {
		JavaType type = Json.objectMapper.getTypeFactory()
				.constructMapType(HashMap.class, keyClass, valueClass);
		return read(Json.objectMapper.readerFor(type));
	}

	public <K, V> Map<K, V> toMap() throws IOException {
		final TypeReference<HashMap> type = new TypeReference<HashMap>() {
		};
		return read(Json.objectMapper.readerFor(type));
	}

	public <E> List<E> toList(Class<E> elementClass) throws IOException {
		JavaType type = Json.objectMapper.getTypeFactory()
				.constructCollectionType(ArrayList.class, elementClass);
		return read(Json.objectMapper.readerFor(type));
	}

	public <E> List<E> toList() throws IOException {
		final TypeReference<ArrayList> type = new TypeReference<ArrayList>() {
		};
		return read(Json.objectMapper.readerFor(type));
	}

	public <E> E[] toArray(Class<E> elementClass) throws IOException {
		final TypeFactory typeFactory = TypeFactory.defaultInstance();
		final ArrayType type = typeFactory.constructArrayType(elementClass);
		return read(Json.objectMapper.readerFor(type));
	}

	public Object[] toArray() throws IOException {
		return toArray(Object.class);
	}

	private <T> T read(ObjectReader reader) throws IOException {
		if (object instanceof String) {
			return reader.readValue((String) object);
		}
		if (object instanceof InputStream) {
			return reader.readValue((InputStream) object);
		}
		if (object instanceof File) {
			return reader.readValue((File) object);
		}
		if (object instanceof byte[]) {
			return reader.readValue((byte[]) object);
		}
		if (object instanceof Reader) {
			return reader.readValue((Reader) object);
		}
		if (object instanceof URL) {
			return reader.readValue((URL) object);
		}
		return reader.readValue((JsonNode) object);
	}
}
