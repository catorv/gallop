package com.catorv.gallop.dataformat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.BitSet;

/**
 * BitSet Serializer
 * Created by cator on 8/1/16.
 */
class BitSetSerializer extends StdSerializer<BitSet> {

	BitSetSerializer() {
		super(BitSet.class);
	}

	@Override
	public void serialize(BitSet v, JsonGenerator jgen, SerializerProvider sp)
			throws IOException {
		long[] longs = v.toLongArray();
		StringBuilder sb = new StringBuilder();
		for (long l : longs) {
			sb.append(l);
			sb.append('|');
		}
		jgen.writeString(sb.toString());
	}

}

