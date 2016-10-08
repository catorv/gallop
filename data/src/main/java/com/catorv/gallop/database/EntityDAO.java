package com.catorv.gallop.database;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import org.hibernate.Session;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Entity Manager DAO
 * Created by cator on 8/12/16.
 */
public class EntityDAO<T> {

	@Inject
	private EntityManager entityManager;

	private Class<T> entityClass;

	protected List<T> emptyList = new ArrayList<>();

	protected EntityDAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	//

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	private Session getSession() {
		return (Session) entityManager.getDelegate();
	}

	//

	public T create() {
		try {
			T entity = entityClass.newInstance();
			persist(entity);
			return entity;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public T save(T entity) {
		if (!contains(entity)) {
			persist(entity);
			return entity;
		}
		return merge(entity);
	}

	public void removeById(Object primaryKey) {
		T entity = find(primaryKey);
		if (entity == null) {
			return;
		}
		remove(entity);
	}

	public void removeAll() {
		CriteriaDelete<T> cd = getCriteriaBuilder().createCriteriaDelete(entityClass);
		cd.from(entityClass);
		createQuery(cd).executeUpdate();
	}

	public void removeByExample(Map<String, Object> parameters) {
		CriteriaDelete<T> cd = getCriteriaBuilder().createCriteriaDelete(entityClass);
		Root<T> entity = cd.from(entityClass);
		for (Predicate predicate : buildPredicates(entity, parameters)) {
			cd.where(predicate);
		}
		createQuery(cd).executeUpdate();
	}

	public List<T> list(int start, int limit) {
		CriteriaQuery<T> query = getCriteriaBuilder().createQuery(entityClass);
		query.from(entityClass);
		TypedQuery<T> typedQuery = createQuery(query);
		typedQuery.setFirstResult(start < 0 ? 0 : start);
		typedQuery.setMaxResults(limit < 1 ? Integer.MAX_VALUE : limit);
		return typedQuery.getResultList();
	}

	public Long count() {
		CriteriaBuilder cb = getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<T> entity = query.from(entityClass);
		query.select(cb.count(entity));
		return entityManager.createQuery(query).getSingleResult();
	}

	public List<T> listByNamedQuery(String queryName,
	                                Map<String, Object> parameters) {
		TypedQuery<T> query = createNamedQuery(queryName);
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
		return query.getResultList();
	}

	public List<T> listByNamedQuery(String queryName) {
		return createNamedQuery(queryName).getResultList();
	}

	public List<T> listByExample(Map<String, Object> parameters) {
		CriteriaQuery<T> query = getCriteriaBuilder().createQuery(entityClass);
		Root<T> entity = query.from(entityClass);
		query.select(entity);
		for (Predicate predicate : buildPredicates(entity, parameters)) {
			query.where(predicate);
		}
		return createQuery(query).getResultList();
	}

	public List<T> listByExample(Map<String, Object> parameters, int start,
	                             int limit) {
		CriteriaQuery<T> query = getCriteriaBuilder().createQuery(entityClass);
		Root<T> entity = query.from(entityClass);
		query.select(entity);
		for (Predicate predicate : buildPredicates(entity, parameters)) {
			query.where(predicate);
		}
		TypedQuery<T> typedQuery = createQuery(query);
		typedQuery.setFirstResult(start < 0 ? 0 : start);
		typedQuery.setMaxResults(limit < 1 ? Integer.MAX_VALUE : limit);
		return typedQuery.getResultList();
	}

	public CriteriaQuery<T> createCriteriaQuery() {
		return getCriteriaBuilder().createQuery(entityClass);
	}

	private List<Predicate> buildPredicates(Root<T> entity,
	                                        Map<String, Object> parameters) {
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder cb = getCriteriaBuilder();
		if (parameters != null) {
			for (String key : parameters.keySet()) {
				Object object = parameters.get(key);
				if (object == null) {
					predicates.add(cb.isNull(entity.get(key)));
				} else {
					predicates.add(cb.equal(entity.get(key), object));
				}
			}
		}
		return predicates;
	}

	// EntityManager Proxy

	private boolean isNullOrEmpty(Object object) {
		return null == object ||
				(object instanceof String) && Strings.isNullOrEmpty((String) object);
	}

	public void persist(Object entity) {
		getEntityManager().persist(entity);
	}

	public T merge(T entity) {
		return getEntityManager().merge(entity);
	}

	public void remove(Object entity) {
		getEntityManager().remove(entity);
	}

	public T find(Object primaryKey) {
		if (isNullOrEmpty(primaryKey)) return null;
		return getEntityManager().find(getEntityClass(), primaryKey);
	}

	public T find(Object primaryKey, Map<String, Object> properties) {
		if (isNullOrEmpty(primaryKey)) return null;
		return getEntityManager().find(getEntityClass(), primaryKey, properties);
	}

	public T find(Object primaryKey, LockModeType lockMode) {
		if (isNullOrEmpty(primaryKey)) return null;
		return getEntityManager().find(getEntityClass(), primaryKey, lockMode);
	}

	public T find(Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
		if (isNullOrEmpty(primaryKey)) return null;
		return getEntityManager().find(getEntityClass(), primaryKey, lockMode, properties);
	}

	public T getReference(Object primaryKey) {
		if (isNullOrEmpty(primaryKey)) return null;
		return getEntityManager().getReference(getEntityClass(), primaryKey);
	}

	public void flush() {
		getEntityManager().flush();
	}

	public void setFlushMode(FlushModeType flushMode) {
		getEntityManager().setFlushMode(flushMode);
	}

	public FlushModeType getFlushMode() {
		return getEntityManager().getFlushMode();
	}

	public void lock(Object entity, LockModeType lockMode) {
		getEntityManager().lock(entity, lockMode);
	}

	public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		getEntityManager().lock(entity, lockMode, properties);
	}

	public void refresh(Object entity) {
		getEntityManager().refresh(entity);
	}

	public void refresh(Object entity, Map<String, Object> properties) {
		getEntityManager().refresh(entity, properties);
	}

	public void refresh(Object entity, LockModeType lockMode) {
		getEntityManager().refresh(entity, lockMode);
	}

	public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		getEntityManager().refresh(entity, lockMode, properties);
	}

	public void clear() {
		getEntityManager().clear();
	}

	public void detach(Object entity) {
		getEntityManager().detach(entity);
	}

	public boolean contains(Object entity) {
		return getEntityManager().contains(entity);
	}

	public LockModeType getLockMode(Object entity) {
		return getEntityManager().getLockMode(entity);
	}

	public void setProperty(String propertyName, Object value) {
		getEntityManager().setProperty(propertyName, value);
	}

	public Map<String, Object> getProperties() {
		return getEntityManager().getProperties();
	}

	public TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		return getEntityManager().createQuery(criteriaQuery);
	}

	public Query createQuery(CriteriaUpdate updateQuery) {
		return getEntityManager().createQuery(updateQuery);
	}

	public Query createQuery(CriteriaDelete deleteQuery) {
		return getEntityManager().createQuery(deleteQuery);
	}

	public TypedQuery<T> createQuery(String qlString) {
		return getEntityManager().createQuery(qlString, getEntityClass());
	}

	public TypedQuery<T> createNamedQuery(String name) {
		return getEntityManager().createNamedQuery(name, getEntityClass());
	}

	public Query createNativeQuery(String sqlString) {
		return getEntityManager().createNativeQuery(sqlString, getEntityClass());
	}

	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		return getEntityManager().createNativeQuery(sqlString, resultSetMapping);
	}

	public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
		return getEntityManager().createNamedStoredProcedureQuery(name);
	}

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
		return getEntityManager().createStoredProcedureQuery(procedureName);
	}

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
		return getEntityManager().createStoredProcedureQuery(procedureName, resultClasses);
	}

	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
		return getEntityManager().createStoredProcedureQuery(procedureName, resultSetMappings);
	}

	public void joinTransaction() {
		getEntityManager().joinTransaction();
	}

	public boolean isJoinedToTransaction() {
		return getEntityManager().isJoinedToTransaction();
	}

	public T unwrap(Class<T> cls) {
		return getEntityManager().unwrap(cls);
	}

	public Object getDelegate() {
		return getEntityManager().getDelegate();
	}

	public void close() {
		getEntityManager().close();
	}

	public boolean isOpen() {
		return getEntityManager().isOpen();
	}

	public EntityTransaction getTransaction() {
		return getEntityManager().getTransaction();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return getEntityManager().getEntityManagerFactory();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return getEntityManager().getCriteriaBuilder();
	}

	public Metamodel getMetamodel() {
		return getEntityManager().getMetamodel();
	}

	public EntityGraph<T> createEntityGraph(Class<T> rootType) {
		return getEntityManager().createEntityGraph(rootType);
	}

	public EntityGraph<?> createEntityGraph(String graphName) {
		return getEntityManager().createEntityGraph(graphName);
	}

	public EntityGraph<?> getEntityGraph(String graphName) {
		return getEntityManager().getEntityGraph(graphName);
	}

	public List<EntityGraph<? super T>> getEntityGraphs() {
		return getEntityManager().getEntityGraphs(getEntityClass());
	}

}
