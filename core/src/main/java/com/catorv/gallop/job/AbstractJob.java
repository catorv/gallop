package com.catorv.gallop.job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractJob<P, R> implements Job<P, R> {

	private String id;

	private TaskExecutor taskExecutor;

	public AbstractJob() {
		this(null);
	}

	protected AbstractJob(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
		this.id = IDGenerator.nextJobID();
	}

	protected abstract List<AbstractJobTask<P, R>> getTasks();

	// TaskID => Shuffler
	protected abstract JobExecuteResultShuffler<P, R> getResultShuffler(AbstractJobTask<P, R> task);

	protected abstract R mergeResults(List<ExecuteResult<JobContext<P, R>>> executeResults);

	@Override
	public R execute(P input) throws Exception {
		List<AbstractJobTask<P, R>> tasks = this.getTasks();
		if (tasks == null || tasks.isEmpty()) {
			return null;
		}

		List<JobContext<P, R>> contexts = new ArrayList<>();
		ExecuteResultList results = new ExecuteResultList();
		FutureList futures = new FutureList();

		boolean first = true;
		for (AbstractJobTask<P, R> task : tasks) {
			if (first) {
				first = false;
				contexts.add(new JobContext<>(this, input));
			} else {
				contexts.clear();
				contexts.addAll(shuffle(task, results));
				results.clear();
				futures.clear();
			}

			for (JobContext<P, R> context : contexts) {
				futures.add(taskExecutor.submit(task, context));
			}

			for (Future<ExecuteResult<JobContext<P, R>>> future : futures.getList()) {
				results.add(future.get());
			}
		}

		return mergeResults(results.getList());
	}

	private List<JobContext<P, R>> shuffle(AbstractJobTask<P, R> task,
	                                       ExecuteResultList results) {
		List<ExecuteResult<JobContext<P, R>>> executeResults = results.getList();

		ExecuteResultShuffler shuffler = getResultShuffler(task);
		if (shuffler != null) {
			return shuffler.shuffle(executeResults);
		}

		// Default Shuffler
		List<JobContext<P, R>> extracted = new ArrayList<>(executeResults.size());
		for (ExecuteResult<JobContext<P, R>> result : executeResults) {
			extracted.add(result.get());
		}
		return extracted;
	}

	@Override
	public String getId() {
		return id;
	}


	/**
	 * Execute Reulst List
	 */
	private class ExecuteResultList
			extends AbstractList<ExecuteResult<JobContext<P, R>>> {
	}


	/**
	 * Job Future List
	 */
	private class FutureList
			extends AbstractList<Future<ExecuteResult<JobContext<P, R>>>> {
	}

	/**
	 * Abstract List
	 */
	private abstract class AbstractList<T> {

		private List<T> list = new ArrayList<>();

		public void add(T result) {
			list.add(result);
		}

		public T get(int index) {
			return list.get(index);
		}

		void clear() {
			list.clear();
		}

		List<T> getList() {
			return list;
		}
	}

}
