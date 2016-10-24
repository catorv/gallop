package com.catorv.gallop.dataformat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.BitSet;

/**
 * Created by cator on 9/3/16.
 */
public final class Xml {

	public static final XmlMapper objectMapper;

	static {
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		SimpleModule module = new SimpleModule("XmlModule", new Version(1, 0,
				0, null, null, null));
		module.addSerializer(new BitSetSerializer());
		module.addDeserializer(BitSet.class, new BitSetDeserializer());
		objectMapper.registerModule(module);
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

	/**
	 * @param source 源数据，支持类型：String InputStream File byte[] Reader URL JsonNode
	 * @return Json Transfer
	 */
	public static DataFormatTransfer of(Object source) {
		return new DataFormatTransfer(source, objectMapper);
	}

}
