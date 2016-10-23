package com.catorv.test.gallop.dataformat;

import java.util.BitSet;

/**
 * Json bean for test
 * Created by cator on 8/1/16.
 */
class JsonBean {

	private String string;

	private Integer integer;

	private BitSet bitSet;

	public String getString() {
		return string;
	}

	void setString(String string) {
		this.string = string;
	}

	public Integer getInteger() {
		return integer;
	}

	void setInteger(Integer integer) {
		this.integer = integer;
	}

	public BitSet getBitSet() {
		return bitSet;
	}

	void setBitSet(BitSet bitSet) {
		this.bitSet = bitSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bitSet == null) ? 0 : bitSet.hashCode());
		result = prime * result + ((integer == null) ? 0 : integer.hashCode());
		result = prime * result + ((string == null) ? 0 : string.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		JsonBean other = (JsonBean) obj;
		if (bitSet == null) {
			if (other.bitSet != null) {
				return false;
			}
		} else if (!bitSet.equals(other.bitSet)) {
			return false;
		}

		if (integer == null) {
			if (other.integer != null) {
				return false;
			}
		} else if (!integer.equals(other.integer)) {
			return false;
		}

		if (string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!string.equals(other.string)) {
			return false;
		}

		return true;
	}
}

