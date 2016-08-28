package com.catorv.test.gallop.database.entity;

import com.catorv.gallop.database.EntityDAO;

/**
 * Created by cator on 8/28/16.
 */
public class ExampleAbstractDAO extends EntityDAO<ExampleAbstractEntity> {
	protected ExampleAbstractDAO() {
		super(ExampleAbstractEntity.class);
	}
}
