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

import java.util.Arrays;
import java.util.List;

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

		ExampleModelWithConfigure model = new ExampleModelWithConfigure(ee);

		assertEquals(ee.getId(), model.getId());
		assertEquals(ee.getName(), model.getName());
		assertEquals(ee.getTitle(), model.getTitle());
		assertEquals(ee.getDesc(), model.getDesc());
		assertEquals(ee.getUrl() + "/index.html", model.getUrl());
		assertEquals(ee.getName().length(), model.getNameLength());

		ExampleModelWithConfigure model1 = Model.of(ee, ExampleModelWithConfigure.class);

		assertEquals(ee.getId(), model1.getId());
		assertEquals(ee.getName(), model1.getName());
		assertEquals(ee.getTitle(), model1.getTitle());
		assertEquals(ee.getDesc(), model1.getDesc());
		assertEquals(ee.getUrl() + "/index.html", model1.getUrl());
		assertEquals(ee.getName().length(), model1.getNameLength());
	}



	@Test
	@Transactional
	public void testList() throws Exception {

		ExampleAbstractEntity ee1 = new ExampleAbstractEntity();
		ee1.setName("cator");
		ee1.setTitle("for test");
		ee1.setDesc("example entity");
		ee1.setUrl("http://catorv.com");

		dao.save(ee1);

		ExampleAbstractEntity ee2 = new ExampleAbstractEntity();
		ee2.setName("cator2");
		ee2.setTitle("for test2");
		ee2.setDesc("example entity2");
		ee2.setUrl("http://catorv.com2");

		dao.save(ee2);

		List<ExampleAbstractEntity> entities = Arrays.asList(ee1, ee2);
		List<ExampleModelWithConfigure> models = Model.listOf(entities, ExampleModelWithConfigure.class);

		assertEquals(2, models.size());

		ExampleModelWithConfigure model1 = models.get(0);

		assertEquals(ee1.getId(), model1.getId());
		assertEquals(ee1.getName(), model1.getName());
		assertEquals(ee1.getTitle(), model1.getTitle());
		assertEquals(ee1.getDesc(), model1.getDesc());
		assertEquals(ee1.getUrl() + "/index.html", model1.getUrl());
		assertEquals(ee1.getName().length(), model1.getNameLength());

		ExampleModelWithConfigure model2 = models.get(1);

		assertEquals(ee2.getId(), model2.getId());
		assertEquals(ee2.getName(), model2.getName());
		assertEquals(ee2.getTitle(), model2.getTitle());
		assertEquals(ee2.getDesc(), model2.getDesc());
		assertEquals(ee2.getUrl() + "/index.html", model2.getUrl());
		assertEquals(ee2.getName().length(), model2.getNameLength());
	}

}
