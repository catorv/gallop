package com.catorv.gallop.service;

import java.io.Serializable;

/**
 * Session
 * Created by cator on 8/15/16.
 */
public interface Session extends Serializable {

	void set(String name, Object object);

	Object get(String name);

	String getString(String name);

	int getInt(String name);

	long getLong(String name);

	double getDouble(String name);

	boolean getBoolean(String name);

}
