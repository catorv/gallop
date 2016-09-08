package com.catorv.gallop.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created by cator on 9/3/16.
 */
public final class Json {

	public static ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		SimpleModule testModule = new SimpleModule("JsonModule", new Version(1, 0,
				0, null, null, null));
		testModule.addSerializer(new BitSetSerializer());
		testModule.addDeserializer(BitSet.class, new BitSetDeserializer());
		objectMapper.registerModule(testModule);
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

	public static String string(Object object) {
		try {
			return objectMapper.writer().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Transfer of(Object source) {
		return new Transfer(source);
	}

	public static class Transfer {

		private Object object;

		Transfer(Object object) {
			this.object = object;
		}

		public String toString() {
			return Json.string(object);
		}

		public byte[] toBytes() throws JsonProcessingException {
			return objectMapper.writer().writeValueAsBytes(object);
		}

		public Transfer to(File file) throws IOException {
			objectMapper.writer().writeValue(file, object);
			return this;
		}

		public Transfer to(OutputStream outputStream) throws IOException {
			objectMapper.writer().writeValue(outputStream, object);
			return this;
		}

		public Transfer to(Writer writer) throws IOException {
			objectMapper.writer().writeValue(writer, object);
			return this;
		}

		public <T> T to(Class<T> clazz) throws IOException {
			return read(objectMapper.readerFor(clazz));
		}

		public <T> T to(JavaType type) throws IOException {
			return read(objectMapper.readerFor(type));
		}

		public <K, V> Map<K, V> toMap() throws IOException {
			final TypeReference<HashMap> type = new TypeReference<HashMap>() {};
			return read(objectMapper.readerFor(type));
		}

		public <E> List<E> toList() throws IOException {
			final TypeReference<ArrayList> type = new TypeReference<ArrayList>() {};
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

}