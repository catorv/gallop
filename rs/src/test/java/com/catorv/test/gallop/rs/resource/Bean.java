package com.catorv.test.gallop.rs.resource;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by cator on 8/17/16.
 */
@XmlRootElement
public class Bean {

	private String str = "String";
	private int i = 123;
	private boolean bool = true;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}
}
