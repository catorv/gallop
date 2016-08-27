package com.catorv.test.gallop.job;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.job.AbstractTask;
import com.catorv.gallop.job.Callback;
import com.catorv.gallop.job.ExecuteResult;
import com.catorv.gallop.job.TaskExecutor;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * the testings for task executor
 * Created by cator on 8/9/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		ConfigurationModule.class,
		LifecycleModule.class
})
public class TaskExecutorTest {

	@Inject
	private TaskExecutor taskExecutor;

	@Test
	public void test() throws ExecutionException, InterruptedException {
		//
		// TaskExecutor (Thread Pool)
		//      | --> TaskWorker --> Task (an executable object)
		//      | --> TaskWorker --> Task
		//      |           .
		//      |           .
		//      |           .
		//      ` --> TaskWorker --> Task
		//
		//
		// TastExecutor.submit(task, input)
		//                |
		//     TaskWorkerManager.lookup() --> TaskWorker (a callable object)
		//                                        |
		//                             submits the TaskWorker --> Future
		//                                                           |
		// ExecuteResult <------------------------------------- Future.get()
		//
		List<Future<ExecuteResult<String>>> futures = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			//noinspection unchecked
			futures.add( taskExecutor.submit(new TestTask(), i,
					new Callback<String>() {
						@Override
						public void call(ExecuteResult<String> result) {
							System.out.println(result.get());
						}
					}
			) );
		}

		List<ExecuteResult<String>> results = new ArrayList<>();
		for (Future<ExecuteResult<String>> future : futures) {
			results.add(future.get());
		}

		Assert.assertEquals(10, results.size());
	}

	private static class TestTask extends AbstractTask<Integer, String> {

		@Override
		public String execute(Integer input) throws Exception {
			Random random = new Random();
			long millis = Math.abs(random.nextLong()) % 1000 + 200;
			Thread.sleep(millis);
			return "Task #" + input + " (" + millis + "ms)";
		}
	}
}
