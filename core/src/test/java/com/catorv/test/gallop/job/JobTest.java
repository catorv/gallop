package com.catorv.test.gallop.job;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.job.*;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.catorv.gallop.util.TypeCast;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * the testings for task executor
 * Created by cator on 8/9/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		ConfigurationModule.class,
		LifecycleModule.class
})
public class JobTest {

	@Inject
	private TaskExecutor taskExecutor;

	@Test
	public void test() throws Exception {
		//
		// job.execute(input)
		//   |
		// input --> task1 --> list1<result> (only one result in this list)
		//                          |
		//                      shuffler1 --> list1<input>
		//                                   /    |  ...  \
		//                                task2 task2 ... task2
		//                                   \    |  ...  /
		//                                   list2<result>
		//                                         |
		//                                     shuffler2 --> list2<input>
		//                                                        .
		//                                                        .
		//                                                        .
		//                                                  listN<result>
		//                                                        |
		// result <---------------------------- mergeResults(listN<input>, input)
		//
		TestJob job = new TestJob(taskExecutor);
		String result = job.execute(0);

		Assert.assertEquals("444444", result);
	}

	private static class TestJob extends AbstractJob<Integer,String> {

		TestShuffler shuffler;

		TestJob(TaskExecutor taskExecutor) {
			super(taskExecutor);
		}

		@Override
		protected List<AbstractJobTask<Integer, String>> getTasks() {
			List<AbstractJobTask<Integer, String>> tasks = new ArrayList<>();
			shuffler = new TestShuffler();
			for (int i = 0; i < 4; i++) {
				TestTask task = new TestTask();
				tasks.add(task);
			}
			return tasks;
		}

		@Override
		protected JobExecuteResultShuffler<Integer, String> getResultShuffler(
				AbstractJobTask<Integer, String> task) {
			return shuffler;
		}

		@Override
		protected String mergeResults(List<ExecuteResult<JobContext<Integer, String>>> executeResults) {
			StringBuilder sb = new StringBuilder();
			for (ExecuteResult<JobContext<Integer, String>> executeResult : executeResults) {
				JobContext<Integer, String> context = executeResult.get();
				sb.append(context.getResult());
			}
			return sb.toString();
		}

	}

	public static class TestShuffler implements JobExecuteResultShuffler<Integer, String> {

		@Override
		public List<JobContext<Integer, String>> shuffle(List<ExecuteResult<JobContext<Integer, String>>> executeResults) {
			List<JobContext<Integer, String>> results = new ArrayList<>();
			for (ExecuteResult<JobContext<Integer, String>> executeResult : executeResults) {
				JobContext<Integer, String> context = executeResult.get();
				int value = TypeCast.intOf(context.getResult());
				for (int i = 0; i < value; i++) {
					results.add(new JobContext<>(context, value));
				}
			}
			return results;
		}
	}

	private static class TestTask extends AbstractJobTask<Integer, String> {

		@Override
		public String execute(Integer input, JobContext<Integer, String> context) throws Exception {
			context.put("test" + input, input);
			return String.valueOf(input + 1);
		}
	}
}
