package com.catorv.gallop.service;

import com.catorv.gallop.util.TypeCast;

/**
 * Abstract Session
 * Created by cator on 8/15/16.
 */
public abstract class AbstractSession implements Session {

	@Override
	public String getString(String name) {
		Object content = get(name);
		return content != null && content instanceof String ? (String) content : "";
	}

	@Override
	public int getInt(String name) {
		Object content = get(name);
		if (content != null) {
			if (content instanceof Integer) {
				return (int) content;
			}
			if (content instanceof String) {
				return TypeCast.intOf((String) content);
			}
		}
		return 0;
	}

	@Override
	public long getLong(String name) {
		Object content = get(name);
		if (content != null) {
			if (content instanceof Long) {
				return (long) content;
			}
			if (content instanceof String) {
				return TypeCast.longOf((String) content);
			}
		}
		return 0;
	}

	@Override
	public double getDouble(String name) {
		Object content = get(name);
		if (content != null) {
			if (content instanceof Double) {
				return (double) content;
			}
			if (content instanceof String) {
				return TypeCast.doubleOf((String) content);
			}
		}
		return 0;
	}

	@Override
	public boolean getBoolean(String name) {
		Object content = get(name);
		if (content != null) {
			if (content instanceof Boolean) {
				return (boolean) content;
			}
			if (content instanceof String) {
				return TypeCast.booleanOf((String) content);
			}
		}
		return false;
	}

}
