package com.catorv.gallop.database.interceptor;

import com.catorv.gallop.database.EntityDAO;
import com.catorv.gallop.log.InjectLogger;
import com.google.common.base.Strings;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.HibernateException;
import org.slf4j.Logger;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Query Method Interceptor
 * Created by cator on 8/13/16.
 */
public class QueryMethodInterceptor implements MethodInterceptor {

	@InjectLogger
	private Logger logger;

	private ConcurrentMap<Method, QueryMethodInvocation> cachedMethods = new ConcurrentHashMap<>();

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		Object aThis = mi.getThis();
		if (!(aThis instanceof EntityDAO)) {
			throw new HibernateException("Subclass of EntityDAO not found: " +
					aThis.getClass().getName());
		}

		final Method method = mi.getMethod();

		QueryMethodInvocation qmi = cachedMethods.get(method);
		if (qmi != null) {
			return qmi.invoke(mi);
		}

		qmi = new QueryMethodInvocation(logger);

		boolean isSelect = false;
		String sql = null;
		Select select = method.getAnnotation(Select.class);
		if (select != null) {
			qmi.setStatementType(QueryMethodInvocation.StatementType.Select);
			isSelect = true;
			sql = select.value();
		} else {
			Count count = method.getAnnotation(Count.class);
			if (count != null) {
				qmi.setStatementType(QueryMethodInvocation.StatementType.Select);
				isSelect = true;
				qmi.setCountStatement(true);
				sql = count.value();
			} else {
				Update update = method.getAnnotation(Update.class);
				if (update != null) {
					qmi.setStatementType(QueryMethodInvocation.StatementType.Update);
					sql = update.value();
				} else {
					Delete delete = method.getAnnotation(Delete.class);
					if (delete != null) {
						qmi.setStatementType(QueryMethodInvocation.StatementType.Delete);
						sql = delete.value();
					}
				}
			}
		}

		Named named = method.getAnnotation(Named.class);
		if (named != null) {
			qmi.setNamedQuery(true);
			sql = named.value();
		} else if (!Strings.isNullOrEmpty(sql) && sql.startsWith("#")) {
			qmi.setNamedQuery(true);
			sql = sql.substring(1);
			named = Names.named(sql);
		}

		if (!isSelect && Strings.isNullOrEmpty(sql)) {
			throw new HibernateException("SQL is null or empty");
		}
		qmi.setSql(sql);

		ParameterNames parameterNames = method.getAnnotation(ParameterNames.class);
		if (parameterNames == null) {
			if (named != null) {
				Class entityClass = ((EntityDAO) aThis).getEntityClass();
				qmi.setParameterNames(getParamterNames(entityClass, named.value()));
			} else if (!Strings.isNullOrEmpty(sql)) {
				qmi.setParameterNames(getParamterNames(sql));
			} else {
				qmi.setParameterNames(new String[0]);
			}
		} else {
			qmi.setParameterNames(parameterNames.value());
		}

		qmi.setNativeQuery(method.getAnnotation(NativeQuery.class) != null);

		cachedMethods.put(method, qmi);

		return qmi.invoke(mi);
	}

	private String[] getParamterNames(Class<?> clazz, String name) {
		NamedQueries namedQueries = clazz.getAnnotation(NamedQueries.class);
		if (namedQueries != null) {
			for (NamedQuery query : namedQueries.value()) {
				if (query.name().equals(name)) {
					return getParamterNames(query.query());
				}
			}
		}
		return new String[0];
	}

	private String[] getParamterNames(String sql) {
		List<String> names = new ArrayList<>();
		Pattern pattern = Pattern.compile(":(\\w+)");
		Matcher matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String matched = matcher.group(1);
			if (!names.contains(matched)) {
				names.add(matched);
			}
		}
		return names.toArray(new String[0]);
	}

}

