package com.catorv.gallop.observable;

/**
 * Event Object
 * Created by cator on 8/1/16.
 */
public class Event {
	/** Notification的名称 */
	private String name;
	/** 发送Notification的时候带的参数对象 */
	private Object object;
	/** Notification监听方法处理后的结果存放对象 */
	private Object result;

	public Event(String name, Object object) {
		this.name = name;
		this.object = object;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
