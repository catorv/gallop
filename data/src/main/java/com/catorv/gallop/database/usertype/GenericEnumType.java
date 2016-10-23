package com.catorv.gallop.database.usertype;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.type.AbstractStandardBasicType;
import org.hibernate.type.TypeResolver;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.LoggableUserType;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Generic Enum Type
 * Created by cator on 8/13/16.
 */
public class GenericEnumType
		implements EnhancedUserType, DynamicParameterizedType, LoggableUserType,
		Serializable {

	public static final String CLASS_NAME = "com.catorv.gallop.database.usertype.GenericEnumType";

	private Class<? extends Enum> enumClass;
	private Method identifierMethod;
	private Method valueOfMethod;
	private int sqlType;

	@SuppressWarnings("unchecked")
	@Override
	public void setParameterValues(Properties parameters) {
		Object enumClassValue = parameters.get("enumClass");
		if (enumClassValue instanceof String) {
			String enumClassName = (String) enumClassValue;
			try {
				enumClass = ReflectHelper.classForName(enumClassName, getClass())
						.asSubclass(Enum.class);
			} catch (ClassNotFoundException e) {
				throw new HibernateException("Enum class not found: " + enumClassName, e);
			}
		} else if (enumClassValue instanceof Class &&
				Enum.class.isAssignableFrom((Class<?>) enumClassValue)) {
			enumClass = (Class<? extends Enum>) enumClassValue;
		} else {
			throw new HibernateException(new IllegalArgumentException("enumClass"));
		}

		String identifierMethodName = parameters.getProperty("identifierMethod",
				"identifier");
		Class<?> identifierType;
		try {
			identifierMethod = enumClass.getMethod(identifierMethodName);
			identifierType = identifierMethod.getReturnType();
		} catch (Exception e) {
			throw new HibernateException("Failed to obtain identifier method", e);
		}

		AbstractStandardBasicType type;
		type = (AbstractStandardBasicType) new TypeResolver().basic(identifierType.getName());
		if (type == null) {
			throw new HibernateException("Unsupported identifier type " + identifierType.getName());
		}

		sqlType = type.getSqlTypeDescriptor().getSqlType();

		String valueOfMethodName = parameters.getProperty("valueOfMethod", "of");
		try {
			valueOfMethod = enumClass.getMethod(valueOfMethodName, identifierType);
		} catch (Exception e) {
			throw new HibernateException("Failed to obtain valueOf method", e);
		}
	}

	@Override
	public int[] sqlTypes() {
		return new int[]{sqlType};
	}

	@Override
	public Class<? extends Enum> returnedClass() {
		return enumClass;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return x == y;
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x == null ? 0 : x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
		if (rs.wasNull()) {
			return null;
		}

		Object identifier = rs.getObject(names[0]);
		try {
			if (identifier == null) {
				return null;
			}
			return valueOfMethod.invoke(enumClass, identifier);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new HibernateException("Exception while invoking valueOf method '" +
					valueOfMethod.getName() + "' of " +
					"enumeration class '" + enumClass + "'", e);
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
		try {
			if (value == null) {
				st.setNull(index, sqlType);
			} else {
				st.setObject(index, identifierMethod.invoke(value), sqlType);
			}
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new HibernateException("Exception while invoking identifierMethod '"
					+ identifierMethod.getName() + "' of " +
					"enumeration class '" + enumClass + "'", e);
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@Override
	public String objectToSQLString(Object value) {
		return '\'' + toXMLString(value) + '\'';
	}

	@Override
	public String toXMLString(Object value) {
		try {
			return String.valueOf(identifierMethod.invoke(value));
		} catch (IllegalAccessException | InvocationTargetException e) {
			return "";
		}
	}

	@Override
	public Object fromXMLString(String xmlValue) {
		try {
			return valueOfMethod.invoke(enumClass, xmlValue);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return null;
		}
	}

	@Override
	public String toLoggableString(Object value, SessionFactoryImplementor factory) {
		return toXMLString(value);
	}

}
