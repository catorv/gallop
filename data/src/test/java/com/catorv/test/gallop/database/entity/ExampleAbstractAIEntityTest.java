package com.catorv.test.gallop.database.entity;

import com.catorv.gallop.CoreModule;
import com.catorv.gallop.database.DatabaseModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.PersistenceException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by cator on 8/28/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		CoreModule.class,
		DatabaseModule.class
})
public class ExampleAbstractAIEntityTest {

	@Inject
	private ExampleAbstractAIDAO dao;

	@Test
	@Transactional
	public void test() throws Exception {

		ExampleAbstractAIEntity ee = new ExampleAbstractAIEntity();
		ee.setName("cator");
		ee.setTitle("for test");
		ee.setDesc("example entity");
		ee.setUrl("http://catorv.com");

		ee.setId("123");

		dao.save(ee);

		assertNotNull(ee.getId());

		ExampleAbstractAIEntity ee1 = dao.find("123");

		assertEquals(ee, ee1);
		assertEquals(ee.getName(), ee1.getName());
		assertEquals(ee.getTitle(), ee1.getTitle());
		assertEquals(ee.getDesc(), ee1.getDesc());
		assertEquals(ee.getUrl(), ee1.getUrl());
	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void test2() throws Exception {

		ExampleAbstractAIEntity ee = new ExampleAbstractAIEntity();
		ee.setName("cator");
		ee.setTitle("for test");
		ee.setDesc("example entity");
		ee.setUrl("http://catorv.com");

		dao.save(ee);
	}
}
