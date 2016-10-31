package com.catorv.gallop.database.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract Entity
 * Created by cator on 8/11/16.
 */
@MappedSuperclass
public class AbstractEmptyEntity implements Entity {

	private static final long serialVersionUID = 5800912977708114323L;

	@Id
	@GeneratedValue(generator = "UUID25GeneratorEmpty")
	@GenericGenerator(
			name = "UUID25GeneratorEmpty",
			strategy = "com.catorv.gallop.database.entity.UUID25Generator"
	)
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
		AbstractEmptyEntity other = (AbstractEmptyEntity) obj;
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
