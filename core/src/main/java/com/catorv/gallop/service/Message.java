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

	Session getSession();

	void setField(String name, Object value);

	Object getField(String name);

	Map<String, Object> getFields();

}
