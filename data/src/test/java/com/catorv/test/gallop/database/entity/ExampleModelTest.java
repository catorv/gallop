package com.catorv.test.gallop.database.entity;

import com.catorv.gallop.CoreModule;
import com.catorv.gallop.database.DatabaseModule;
import com.catorv.gallop.database.Model;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by cator on 8/28/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		CoreModule.class,
		DatabaseModule.class
})
public class ExampleModelTest {

	@Inject
	private ExampleAbstractDAO dao;

	@Test
	@Transactional
	public void test() throws Exception {

		ExampleAbstractEntity ee = new ExampleAbstractEntity();
		ee.setName("cator");
		ee.setTitle("for test");
		ee.setDesc("example entity");
		ee.setUrl("http://catorv.com");

		dao.save(ee);

		ExampleModel model = new ExampleModel(ee);

		assertEquals(ee.getId(), model.getId());
		assertEquals(ee.getName(), model.getName());
		assertEquals(ee.getTitle(), model.getTitle());
		assertEquals(ee.getDesc(), model.getDesc());
		assertEquals(ee.getUrl(), model.getUrl());

		ExampleModel model1 = Model.of(ee, ExampleModel.class);

		assertEquals(ee.getId(), model1.getId());
		assertEquals(ee.getName(), model1.getName());
		assertEquals(ee.getTitle(), model1.getTitle());
		assertEquals(ee.getDesc(), model1.getDesc());
		assertEquals(ee.getUrl(), model1.getUrl());
	}

	@Test
	@Transactional
	public void test2() throws Exception {

		ExampleAbstractEntity ee = new ExampleAbstractEntity();
		ee.setName("cator");
		ee.setTitle("for test");
		ee.setDesc("example entity");
		ee.setUrl("http://catorv.com");

		dao.save(ee);

		ExampleModel2 model = new ExampleModel2(ee);

		assertEquals(ee.getId(), model.getId());
		assertEquals(ee.getName(), model.getName());
		assertEquals(ee.getTitle(), model.getTitle());
		assertEquals(ee.getDesc(), model.getDesc());
		assertEquals(ee.getUrl() + "/index.html", model.getUrl());
		assertEquals(ee.getName().length(), model.getNameLength());

		ExampleModel2 model1 = Model.of(ee, ExampleModel2.class);

		assertEquals(ee.getId(), model1.getId());
		assertEquals(ee.getName(), model1.getName());
		assertEquals(ee.getTitle(), model1.getTitle());
		assertEquals(ee.getDesc(), model1.getDesc());
		assertEquals(ee.getUrl() + "/index.html", model1.getUrl());
		assertEquals(ee.getName().length(), model1.getNameLength());
	}
}
