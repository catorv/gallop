package com.catorv.test.gallop.database.entity;

import com.catorv.gallop.database.entity.EntityModel;

/**
 * Created by cator on 8/28/16.
 */
public class ExampleEntityModel extends EntityModel<ExampleAbstractEntity> {

	private String id;
	private String name;
	private String title;
	private String desc;
	private String url;

	public ExampleEntityModel(ExampleAbstractEntity entity) {
		super(entity);
	}

	public String getId() {
		return id;
	}

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
