package com.catorv.gallop.httpclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Query String
 * Created by cator on 8/15/16.
 */
public class QueryString extends HashMap<String, Object> {

	private Charset charset = Charset.defaultCharset();

	public QueryString() {
	}

	public QueryString(Map<String, Object> map, Charset charset) {
		this(map);
		this.charset = charset;
	}

	public QueryString(Map<String, Object> map) {
		putAll(map);
	}

	public QueryString(Charset charset) {
		this.charset = charset;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String charEncoding = charset.name();
		boolean first = true;
		for (Map.Entry<String, Object> entry : entrySet()) {
			if (!first) {
				sb.append("&");
			} else {
				first = false;
			}
			try {
				sb.append(URLEncoder.encode(entry.getKey(), charEncoding));
				sb.append("=");

				Object value = entry.getValue();
				if (value != null) {
					sb.append(URLEncoder.encode(String.valueOf(value), charEncoding));
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return sb.toString();
	}
}
