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
public class AbstractEntity implements Entity {

	private static final long serialVersionUID = 9005668154934951558L;

	@Id
	@GeneratedValue(generator = "UUID25Generator")
	@GenericGenerator(
			name = "UUID25Generator",
			strategy = "com.catorv.gallop.database.entity.UUID25Generator"
	)
	@Column(length = 64)
	private String id;

	@Column(length = 127)
	private String name;

	@Column(length = 255)
	private String title;

	@Column(length = 512, name = "description")
	private String desc;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
		AbstractEntity other = (AbstractEntity) obj;
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
