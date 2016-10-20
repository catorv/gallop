package com.catorv.test.gallop.database;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.database.DatabaseModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicLong;

@RunWith(GuiceTestRunner.class)
@GuiceModule({
		ConfigurationModule.class,
		LifecycleModule.class,
		DatabaseModule.class
})
public class ExampleEntityTransactionalNestedTest {

	@Inject
	private ExampleDAO dao;

	@Before
	@Transactional
	public void before() {
		dao.removeAll();
	}

	private static AtomicLong num = new AtomicLong(0);

	@Test
	@Transactional
	public void test() {
		ExampleEntity ee = new ExampleEntity();
		ee.setId(num.incrementAndGet());
		ee.setName("example entity");
		save(ee);

		ExampleEntity ee2 = dao.find(1L);

		Assert.assertEquals(ee, ee2);
	}

	@Transactional
	public void save(ExampleEntity ee) {
		dao.persist(ee);
		flush();
	}

	@Transactional
	public void flush() {
		dao.flush();
		dao.clear();
	}

}
