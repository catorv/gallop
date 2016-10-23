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

	private Map<String, Object> info = new HashMap<>();

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
		return null;
	}

	@Override
	public ServiceException getException() {
		return exception;
	}

	@Override
	public void setInfo(String name, Object value) {
		info.put(name, value);
	}

	@Override
	public Object getInfo(String name) {
		return info.get(name);
	}

	@Override
	public Map<String, Object> getAllInfo() {
		return info;
	}

	public String getInfoAsString(String name, @Nullable String defaultValue) {
		final Object value = getInfo(name);
		if (value == null) return defaultValue;
		return String.valueOf(value);
	}

	public int getInfoAsInt(String name, int defaultValue) {
		final Object value = getInfo(name);
		if (value == null) return defaultValue;
		return (int) value;
	}

	public long getInfoAsLong(String name, long defaultValue) {
		final Object value = getInfo(name);
		if (value == null) return defaultValue;
		return (long) value;
	}

	public double getInfoAsDouble(String name, double defaultValue) {
		final Object value = getInfo(name);
		if (value == null) return defaultValue;
		return (double) value;
	}

	public boolean getInfoAsBoolean(String name, boolean defaultValue) {
		final Object value = getInfo(name);
		if (value == null) return defaultValue;
		return (boolean) value;
	}

}
