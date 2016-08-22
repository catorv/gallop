package com.catorv.gallop.inject;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * the implementation for Namespace
 * Created by cator on 8/3/16.
 */
class NamespaceImpl implements Namespace, Serializable {
	private final String value;
	private static final long serialVersionUID = 0L;

	public NamespaceImpl(String value) {
		this.value = Preconditions.checkNotNull(value, "name");
	}

	public String value() {
		return this.value;
	}

	public int hashCode() {
		return 127 * "value".hashCode() ^ this.value.hashCode();
	}

	public boolean equals(Object o) {
		if(!(o instanceof Namespace)) {
			return false;
		} else {
			Namespace other = (Namespace) o;
			return this.value.equals(other.value());
		}
	}

	public String toString() {
		return "@" + Namespace.class.getName() + "(value=" + this.value + ")";
	}

	public Class<? extends Annotation> annotationType() {
		return Namespace.class;
	}
}

