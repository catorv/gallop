package com.catorv.gallop.service;

import java.io.Serializable;
import java.util.Map;

/**
 * Message
 * Created by cator on 8/15/16.
 */
public interface Message<T> extends Serializable {

	ServiceException getException();

	T getContent();

	void setProperty(String name, Object value);

	Object getProperty(String name);

	Map<String, Object> getProperties();

}
