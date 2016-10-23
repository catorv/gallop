package com.catorv.gallop.dataformat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * BitSet Deserializer
 * Created by cator on 8/1/16.
 */
class BitSetDeserializer extends JsonDeserializer<BitSet> {

	@Override
	public BitSet deserialize(JsonParser jp, DeserializationContext dc)
			throws IOException {
		String text = jp.getText();
		int i;
		int j = 0;
		String s;

		List<Long> values = new ArrayList<>();
		while ((i = text.indexOf('|', j)) > -1) {
			s = text.substring(j, i);
			j = i + 1;
			if (!s.isEmpty()) {
				values.add(Long.valueOf(s));
			}
		}
		if (j < text.length()) {
			values.add(Long.valueOf(text.substring(j)));
		}
		long[] longs = new long[values.size()];
		i = 0;
		for (Long l : values) {
			longs[i++] = l;
		}
		return BitSet.valueOf(longs);
	}

}
