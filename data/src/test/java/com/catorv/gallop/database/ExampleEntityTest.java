package com.catorv.gallop.database;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(GuiceTestRunner.class)
@GuiceModule({
		ConfigurationModule.class,
		LifecycleModule.class,
		DatabaseModule.class
})
public class ExampleEntityTest {

	@Inject
	private ExampleEntityDAO dao;

	@Before
	@Transactional
	public void before() {
		dao.removeAll();
	}

	@Test
	@Transactional
	public void test() {

		ExampleEntity ee = new ExampleEntity();
		ee.setId(1L);
		String s = new String(new byte[]{(byte) 0xf0, (byte) 0x9f, (byte) 0x92, (byte) 0x83});
		ee.setName("example entity" + s);
		dao.persist(ee);
		dao.flush();
		dao.clear();
		ExampleEntity ee2 = dao.find(1L);

		Assert.assertEquals(ee, ee2);
	}

	@Test
	@Transactional
	public void testCount() {

		for (Long i = 10L; i < 20; i++) {
			ExampleEntity ee = new ExampleEntity();
			ee.setId(i);
			ee.setName("cator" + i);
			dao.save(ee);
		}

		long count = dao.count();

		Assert.assertEquals(10, count);
	}

	@Test
	@Transactional
	public void testSelect() {

		for (Long i = 20L; i < 30; i++) {
			ExampleEntity ee = new ExampleEntity();
			ee.setId(i);
			ee.setName("cator" + i);
			dao.save(ee);
		}

		List<ExampleEntity> ees = dao.list();
		long count = dao.countAnnotated();

		Assert.assertEquals(4, ees.size());
		Assert.assertEquals(10, count);
	}

	@Test
	@Transactional
	public void testSelectNamed() {

		for (Long i = 30L; i < 40; i++) {
			ExampleEntity ee = new ExampleEntity();
			ee.setId(i);
			ee.setName("cator" + i);
			dao.save(ee);
		}

		List<ExampleEntity> ees = dao.list2();

		Assert.assertEquals(4, ees.size());
	}

	@Test
	@Transactional
	public void testGetByName() {

		ExampleEntity ee = new ExampleEntity();
		ee.setId(1000L);
		ee.setName("cator1000");
		dao.save(ee);

		ExampleEntity ee1 = dao.getByName("cator1000");

		Assert.assertEquals(1000, (long) ee1.getId());
	}

	@Test
	@Transactional
	public void testCountByName() {

		for (Long i = 40L; i < 50; i++) {
			ExampleEntity ee = new ExampleEntity();
			ee.setId(i);
			ee.setName("cator-hahaha");
			dao.save(ee);
		}

		long count = dao.countByName("cator-hahaha");

		Assert.assertEquals(10, count);
	}

	@Test
	@Transactional
	public void testMultiRecord() {

		for (Long i = 50L; i < 60; i++) {
			ExampleEntity ee = new ExampleEntity();
			ee.setId(i);
			ee.setName("cator" + i);
			dao.save(ee);
		}

		List<ExampleEntity> ees = dao.list3(52L, 56L);

		Assert.assertEquals(3, ees.size());
	}

	@Test
	@Transactional
	public void testNativeQuery() {

		for (Long i = 60L; i < 70; i++) {
			ExampleEntity ee = new ExampleEntity();
			ee.setId(i);
			ee.setName("cator" + i);
			dao.save(ee);
		}

		List<ExampleEntity> ees = dao.list4();

		Assert.assertEquals(4, ees.size());
	}

}
