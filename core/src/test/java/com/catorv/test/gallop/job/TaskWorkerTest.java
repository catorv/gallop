package com.catorv.test.gallop.job;

import com.catorv.gallop.job.AbstractTask;
import com.catorv.gallop.job.Callback;
import com.catorv.gallop.job.ExecuteResult;
import com.catorv.gallop.job.worker.LocalTaskWorkerManager;
import com.catorv.gallop.job.worker.TaskWorker;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * TaskWorker Test
 * Created by cator on 8/9/16.
 */
@RunWith(GuiceTestRunner.class)
public class TaskWorkerTest {

	@Inject
	private LocalTaskWorkerManager taskWorkerManager;

	@Test
	public void testLocalTask() throws Exception {
		TestTask task = new TestTask();

		final int[] result = new int[1];
		@SuppressWarnings("unchecked")
		TaskWorker<Integer, String> worker = taskWorkerManager.lookup(task, 123567,
				new Callback<String>() {
					@Override
					public void call(ExecuteResult<String> param) {
						result[0] = Integer.valueOf(param.get().substring(8));
					}
				}
		);

		ExecuteResult<String> executeResult = worker.call();

		Assert.assertEquals("Result: 123567", executeResult.get());
		Assert.assertEquals(123567, result[0]);
	}

	private static class TestTask extends AbstractTask<Integer, String> {

		protected TestTask() {
		}

		@Override
		public String execute(Integer input) throws Exception {
			return "Result: " + input;
		}
	}

}
