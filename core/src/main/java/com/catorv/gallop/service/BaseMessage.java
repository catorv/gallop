package com.catorv.gallop.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Message
 * Created by cator on 8/15/16.
 */
public class BaseMessage<T> implements Message<T> {

	private ServiceException exception;

	private T content;

	private Map<String, Object> properties = new HashMap<>();

	public BaseMessage(ServiceException exception) {
		this.exception = exception;
	}

	public BaseMessage(T content) {
		this.content = content;
	}

	@Override
	public T getContent() {
		return content;
	}

	@Override
	public ServiceException getException() {
		return exception;
	}

	@Override
	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}

	@Override
	public Object getProperty(String name) {
		return properties.get(name);
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}
}
