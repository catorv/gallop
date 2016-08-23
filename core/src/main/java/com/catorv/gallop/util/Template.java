package com.catorv.gallop.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字符串模板类
 * Created by cator on 8/2/16.
 */
public class Template {

	private static final char RB = '}';
	private static final char LB = '{';
	private static final char ESCAPE = '\\';

	private List<CompiledUnit> compiledTemplate = null;

	private static Cache<String, Template> cache = CacheBuilder
			.newBuilder().maximumSize(1024).build();

	public static Template of(String template) {
		String key = DigestUtils.md5(template);
		Template tpl = cache.getIfPresent(key);
		if (tpl == null) {
			tpl = new Template(template);
			cache.put(key, tpl);
		}
		return tpl;
	}

	private Template(String template) {
		compiledTemplate = compile(template);
	}

	private List<CompiledUnit> compile(String template) {
//		template = template.trim();
		List<CompiledUnit> parts = new ArrayList<>();
		char[] chars = template.toCharArray();
		int state = 0;
		int pos = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (state == 0) {
				if (c == ESCAPE) {
					sb.append(chars, pos, i - pos);
					pos = i + 1;
					state = 1;
				} else if (c == LB) {
					sb.append(chars, pos, i - pos);
					parts.add(new CompiledUnit(sb.toString(), UnitType.Static));
					sb.setLength(0);
					pos = i + 1;
					state = 2;
				}
			} else if (state == 1) {
				state = 0;
			} else if (state == 2) {
				if (c == ESCAPE) {
					sb.append(chars, pos, i - pos);
					pos = i + 1;
					state = 3;
				} else if (c == RB) {
					sb.append(chars, pos, i - pos);
					parts.add(new CompiledUnit(sb.toString(), UnitType.Dynamic));
					sb.setLength(0);
					pos = i + 1;
					state = 0;
				}
			} else {// if (state == 3) {
				state = 2;
			}
		}
		if (state != 0) {
			pos--;
		}
		if (pos < chars.length) {
			sb.append(chars, pos, chars.length - pos);
			parts.add(new CompiledUnit(sb.toString(), UnitType.Static));
		}

		return parts;
	}

	private static enum UnitType {
		Static, Dynamic
	}

	private static class CompiledUnit {

		private UnitType type;

		private String content;

		CompiledUnit(String content, UnitType type) {
			this.type = type;
			this.content = content;
		}
	}

	public String evaluate(String... pairs) {
		if (pairs != null && pairs.length % 2 != 0) {
			throw new IllegalArgumentException("input values should be in pairs");
		}
		Map<String, String> params = new HashMap<String, String>();
		if (pairs != null) {
			for (int i = 0; i < pairs.length; i += 2) {
				params.put(pairs[i], pairs[i + 1]);
			}
		}
		StringBuilder sb = new StringBuilder(256);
		String value;
		for (CompiledUnit part : compiledTemplate) {
			if (UnitType.Static == part.type) {
				sb.append(part.content);
			} else {
				value = params.get(part.content);
				if (value != null) {
					sb.append(value);
				} else if (!params.containsKey(part.content)) {
					sb.append(LB);
					sb.append(part.content);
					sb.append(RB);
				}
			}
		}
		return sb.toString();
	}

}

