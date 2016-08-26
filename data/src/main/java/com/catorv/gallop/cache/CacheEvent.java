package com.catorv.gallop.cache;

/**
 * Cache Event
 * Created by cator on 8/3/16.
 */
public class CacheEvent<K, V> {

	public static enum Type {
		PUT,
		GET,
		INVALIDATE
	}

	private K key;

	private V value;

	private Type type;

	public CacheEvent(Type type, K key, V value) {
		this.type = type;
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}

