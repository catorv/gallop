package com.catorv.gallop.database.entity;

import com.catorv.gallop.util.UUID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * Created by cator on 9/1/16.
 */
public class UUID25Generator implements IdentifierGenerator {
	@Override
	public Serializable generate(SharedSessionContractImplementor session,
	                             Object object)
			throws HibernateException {
		return UUID.getUUID25();
	}
}
