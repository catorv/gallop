package com.catorv.gallop.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base Session
 * Created by cator on 8/15/16.
 */
public class BaseSession extends AbstractSession {

	private static final long serialVersionUID = 3173527760794432323L;

	private Map<String, Object> map = new ConcurrentHashMap<>();

	@Override
	public void set(String name, Object object) {
		map.put(name, object);
	}

	@Override
	public Object get(String name) {
		return map.get(name);
	}

}
