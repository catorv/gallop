package com.catorv.gallop.lifecycle;

import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 对象生命周期管理模块测试
 * Created by cator on 6/21/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		LifecycleModule.class
})
public class LifecycleTest {

	public static class Foo {
		int value = 0;

		@Initialize
		public void init() {
			this.value += 1;
		}

		@Destroy
		public void destroy() {
			this.value -= 1;
			System.out.println("[INFO] The destroy method of a instance of the class Foo is invoked.");
		}
	}

	@Inject
	private Foo foo;

	@Inject
	private Foo foo2;

	@Test
	public void test() {
		Assert.assertEquals(1, foo.value);

		foo2.value = 4;
		ShutdownHook.callDestroyMethod(foo2);
		Assert.assertEquals(3, foo2.value);
	}

}
