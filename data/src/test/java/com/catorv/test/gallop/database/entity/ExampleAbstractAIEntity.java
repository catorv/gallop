package com.catorv.test.gallop.database.entity;

import com.catorv.gallop.database.entity.AbstractAssignedIdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by cator on 8/27/16.
 */
@Entity
@Table(name = "test2")
public class ExampleAbstractAIEntity extends AbstractAssignedIdEntity {

	private static final long serialVersionUID = -4541638342634371777L;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
