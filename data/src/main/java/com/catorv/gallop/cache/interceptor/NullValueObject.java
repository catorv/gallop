package com.catorv.gallop.cache.interceptor;

import java.io.Serializable;

/**
 * Null value object
 * Created by cator on 8/3/16.
 */
class NullValueObject implements Serializable {

	private static final long serialVersionUID = 2923393912704012362L;

	private int dummy = 0;

	public int getDummy() {
		return dummy;
	}

	public void setDummy(int dummy) {
		this.dummy = dummy;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj != null) && (obj instanceof NullValueObject);
	}
}

