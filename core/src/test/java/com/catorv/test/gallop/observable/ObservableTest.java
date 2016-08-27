package com.catorv.test.gallop.observable;

import com.catorv.gallop.observable.*;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * Created by cator on 8/1/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
	ObservableModule.class
})
public class ObservableTest {

	@Observing
	public static class Foo {

		public int count = 0;

		@Observe("add")
		public void add(Event event) {
			if (event.getObject() instanceof Integer) {
				count += (Integer) event.getObject();
			}
		}

	}

	@Inject
	private Foo foo;

	@Inject
	private ObservableManager manager;

	@Test
	public void test() {
		Assert.assertEquals(foo.count, 0);
		manager.notifyObservers(new Event("add", 1));
		Assert.assertEquals(foo.count, 1);
		manager.notifyObservers(new Event("add", 3));
		Assert.assertEquals(foo.count, 4);

		manager.getObservable("add").deleteObservers();
		manager.notifyObservers(new Event("add", 3));
		Assert.assertEquals(foo.count, 4);
	}

}
