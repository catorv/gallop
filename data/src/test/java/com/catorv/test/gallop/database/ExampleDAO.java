package com.catorv.test.gallop.database;

import com.catorv.gallop.database.EntityDAO;
import com.catorv.gallop.database.interceptor.Count;
import com.catorv.gallop.database.interceptor.NativeQuery;
import com.catorv.gallop.database.interceptor.ParameterNames;
import com.catorv.gallop.database.interceptor.Select;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;

import java.util.List;

@Singleton
@Transactional
public class ExampleDAO extends EntityDAO<ExampleEntity> {

	public ExampleDAO() {
		super(ExampleEntity.class);
	}

	@Select("FROM ExampleEntity WHERE id > 25 AND id < 30")
	public List<ExampleEntity> list() {
		return emptyList;
	}

	@Select
	@Named("ExampleList")
	public List<ExampleEntity> list2() {
		return emptyList;
	}

	@Select
	@Named("Query")
//	@ParameterNames({"id1", "id2"})
	public List<ExampleEntity> list3(Long id1, Long id2) {
		return emptyList;
	}

	@Select("SELECT * FROM example WHERE id > 63 AND id < 68")
	@NativeQuery
	public List<ExampleEntity> list4() {
		return emptyList;
	}

	@Count
	public Long countAnnotated() {
		return 0L;
	}

	@Select
	@ParameterNames({"name"})
	public ExampleEntity getByName(String name) {
		return null;
	}

	@Count
	@ParameterNames({"name"})
	public Long countByName(String name) {
		return 0L;
	}

}
