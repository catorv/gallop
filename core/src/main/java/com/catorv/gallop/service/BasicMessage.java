package com.catorv.gallop.service;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Message
 * Created by cator on 8/15/16.
 */
public class BasicMessage<T> implements Message<T> {

	private ServiceException exception;

	private T content;

	private Session session;

	private Map<String, Object> fields = new HashMap<>();

	public BasicMessage(ServiceException exception, Session session) {
		this.exception = exception;
		this.session = session;
	}

	public BasicMessage(T content, Session session) {
		this.content = content;
		this.session = session;
	}

	@Override
	public T getContent() {
		return content;
	}

	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public ServiceException getException() {
		return exception;
	}

	@Override
	public void setField(String name, Object value) {
		fields.put(name, value);
	}

	@Override
	public Object getField(String name) {
		return fields.get(name);
	}

	@Override
	public Map<String, Object> getFields() {
		return fields;
	}

	public String getFieldAsString(String name, @Nullable String defaultValue) {
		final Object value = getField(name);
		if (value == null) return defaultValue;
		return String.valueOf(value);
	}

	public int getFieldAsInt(String name, int defaultValue) {
		final Object value = getField(name);
		if (value == null) return defaultValue;
		return (int) value;
	}

	public long getFieldAsLong(String name, long defaultValue) {
		final Object value = getField(name);
		if (value == null) return defaultValue;
		return (long) value;
	}

	public double getFieldAsDouble(String name, double defaultValue) {
		final Object value = getField(name);
		if (value == null) return defaultValue;
		return (double) value;
	}

	public boolean getFieldAsBoolean(String name, boolean defaultValue) {
		final Object value = getField(name);
		if (value == null) return defaultValue;
		return (boolean) value;
	}

}
