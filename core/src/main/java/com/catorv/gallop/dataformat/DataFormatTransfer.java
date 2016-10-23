package com.catorv.gallop.dataformat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	private ObjectMapper objectMapper;
	private Object object;

	DataFormatTransfer(Object object, ObjectMapper objectMapper) {
		this.object = object;
		this.objectMapper = objectMapper;
	}

	public String toString() {
		try {
			return objectMapper.writer().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] toBytes() throws JsonProcessingException {
		return objectMapper.writer().writeValueAsBytes(object);
	}

	public DataFormatTransfer to(File file) throws IOException {
		objectMapper.writer().writeValue(file, object);
		return this;
	}

	public DataFormatTransfer to(OutputStream outputStream) throws IOException {
		objectMapper.writer().writeValue(outputStream, object);
		return this;
	}

	public DataFormatTransfer to(Writer writer) throws IOException {
		objectMapper.writer().writeValue(writer, object);
		return this;
	}

	public <T> T to(Class<T> clazz) throws IOException {
		return read(objectMapper.readerFor(clazz));
	}

	public <T> T to(JavaType type) throws IOException {
		return read(objectMapper.readerFor(type));
	}

	public <K, V> Map<K, V> toMap(Class<K> keyClass, Class<V> valueClass)
			throws IOException {
		JavaType type = objectMapper.getTypeFactory()
				.constructMapType(HashMap.class, keyClass, valueClass);
		return read(objectMapper.readerFor(type));
	}

	public <K, V> Map<K, V> toMap() throws IOException {
		final TypeReference<HashMap> type = new TypeReference<HashMap>() {
		};
		return read(objectMapper.readerFor(type));
	}

	public <E> List<E> toList(Class<E> elementClass) throws IOException {
		JavaType type = objectMapper.getTypeFactory()
				.constructCollectionType(ArrayList.class, elementClass);
		return read(objectMapper.readerFor(type));
	}

	public <E> List<E> toList() throws IOException {
		final TypeReference<ArrayList> type = new TypeReference<ArrayList>() {
		};
		return read(objectMapper.readerFor(type));
	}

	public <E> E[] toArray(Class<E> elementClass) throws IOException {
		final TypeFactory typeFactory = TypeFactory.defaultInstance();
		final ArrayType type = typeFactory.constructArrayType(elementClass);
		return read(objectMapper.readerFor(type));
	}

	public Object[] toArray() throws IOException {
		return toArray(Object.class);
	}

	public JsonNode toTree() throws IOException {
		if (object instanceof String) {
			return objectMapper.readTree((String) object);
		}
		if (object instanceof InputStream) {
			return objectMapper.readTree((InputStream) object);
		}
		if (object instanceof File) {
			return objectMapper.readTree((File) object);
		}
		if (object instanceof byte[]) {
			return objectMapper.readTree((byte[]) object);
		}
		if (object instanceof Reader) {
			return objectMapper.readTree((Reader) object);
		}
		if (object instanceof URL) {
			return objectMapper.readTree((URL) object);
		}
		return objectMapper.valueToTree(object);
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
