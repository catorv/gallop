package com.catorv.gallop.database.interceptor;

import com.catorv.gallop.database.EntityDAO;
import com.google.common.base.Strings;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.query.Query;
import org.slf4j.Logger;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;

/**
 * Query Method Invocation
 * Created by cator on 8/13/16.
 */
class QueryMethodInvoker {

	private Logger logger;

	private boolean isNamedQuery = false;
	private boolean isNativeQuery = false;
	private boolean isCountStatement = false;

	private StatementType statementType = StatementType.Select;

	private String sql;
	private String[] parameterNames;

	private Query query;

	public QueryMethodInvoker(Logger logger) {
		this.logger = logger;
	}

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		if (Strings.isNullOrEmpty(sql)) {
			return invokeWithoutSql(methodInvocation);
		} else {
			return invokeWithSql(methodInvocation);
		}
	}

	private Object invokeWithSql(MethodInvocation methodInvocation) throws Throwable {
		EntityDAO dao = (EntityDAO) methodInvocation.getThis();

		if (query == null) {
			if (isNamedQuery) {
				query = (Query) dao.createNamedQuery(sql);
			} else if (isNativeQuery) {
				query = (Query) dao.createNativeQuery(sql);
			} else {
				query = (Query) dao.createQuery(sql);
			}
		}

		Object[] arguments = methodInvocation.getArguments();
		int i = 0;
		for(; i < parameterNames.length; i++) {
			query.setParameter(parameterNames[i], arguments[i]);
		}
		if (!isCountStatement && arguments.length - i == 2) {
			query.setFirstResult((Integer) arguments[i++]);
			query.setMaxResults((Integer) arguments[i]);
		}

		if (statementType == StatementType.Select) {
			return getResult(methodInvocation, query);
		}

		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public Object invokeWithoutSql(MethodInvocation methodInvocation) throws Throwable {
		EntityDAO dao = (EntityDAO) methodInvocation.getThis();

		CriteriaBuilder cb = dao.getCriteriaBuilder();

		CriteriaQuery criteriaQuery;
		if (isCountStatement()) {
			criteriaQuery = cb.createQuery(Long.class);
		} else {
			criteriaQuery = dao.createCriteriaQuery();
		}

		Root entity = criteriaQuery.from(dao.getEntityClass());
		if (isCountStatement) {
			criteriaQuery.select(cb.count(entity));
		}

		Object[] arguments = methodInvocation.getArguments();
		int i = 0;
		for(; i < parameterNames.length; i++) {
			String key = parameterNames[i];
			Object object = arguments[i];
			if (object == null) {
				criteriaQuery.where(cb.isNull(entity.get(key)));
			} else {
				criteriaQuery.where(cb.equal(entity.get(key), object));
			}
		}

		TypedQuery typeQuery = dao.createQuery(criteriaQuery);
		if (!isCountStatement && arguments.length - i == 2) {
			typeQuery.setFirstResult((Integer) arguments[i++]);
			typeQuery.setMaxResults((Integer) arguments[i]);
		}

		return getResult(methodInvocation, typeQuery);
	}

	private Object getResult(MethodInvocation methodInvocation, TypedQuery query)
			throws Throwable {
		Method method = methodInvocation.getMethod();
		Object result;
		if (isCountStatement) {
			result = query.getSingleResult();
		} else if (Iterable.class.isAssignableFrom(method.getReturnType())) {
			result = query.getResultList();
		} else {
			query.setMaxResults(1);
			result = query.getSingleResult();
		}

		return result == null ? methodInvocation.proceed() : result;
	}

	public boolean isNamedQuery() {
		return isNamedQuery;
	}

	public void setNamedQuery(boolean namedQuery) {
		isNamedQuery = namedQuery;
	}

	public boolean isNativeQuery() {
		return isNativeQuery;
	}

	public void setNativeQuery(boolean nativeQuery) {
		isNativeQuery = nativeQuery;
	}

	public boolean isCountStatement() {
		return isCountStatement;
	}

	public void setCountStatement(boolean countStatement) {
		isCountStatement = countStatement;
	}

	public StatementType getStatementType() {
		return statementType;
	}

	public void setStatementType(StatementType statementType) {
		this.statementType = statementType;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String[] getParameterNames() {
		return parameterNames;
	}

	public void setParameterNames(String[] parameterNames) {
		this.parameterNames = parameterNames;
	}

	static enum StatementType {
		Select, Delete, Update
	}

}
