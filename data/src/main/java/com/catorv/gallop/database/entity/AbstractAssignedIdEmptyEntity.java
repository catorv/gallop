package com.catorv.gallop.database.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract Assigned Id Entity
 * Created by cator on 8/11/16.
 */
@MappedSuperclass
public class AbstractAssignedIdEmptyEntity implements Entity {

	private static final long serialVersionUID = -2649356894400720813L;

	@Id
	@Column(length = 40)
	private String id;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AbstractAssignedIdEmptyEntity other = (AbstractAssignedIdEmptyEntity) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
