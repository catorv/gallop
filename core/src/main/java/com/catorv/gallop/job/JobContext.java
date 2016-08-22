package com.catorv.gallop.job;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Job Context
 * Created by cator on 8/10/16.
 */
public class JobContext<P, R> {

	private Job<P, R> job;

	private P input;

	private R result;

	private Map<Object, Object> data;

	private JobContext(Job<P, R> job, P input, Map<Object, Object> data) {
		this.job = job;
		this.input = input;
		this.data = data;
	}

	public JobContext(Job<P, R> job, P input) {
		this(job, input, new ConcurrentHashMap<>());
	}

	public JobContext(JobContext<P, R> context, P input) {
		this(context.getJob(), input, context.data);
	}

	public Job<P, R> getJob() {
		return job;
	}

	public P getInput() {
		return input;
	}

	public R getResult() {
		return result;
	}

	public void setResult(R result) {
		this.result = result;
	}

	/**
	 * 设置数据项,如果value=null,则删除该key对应的数据
	 * @param key key
	 * @param value value
	 * @return 设置或删除数据项的前一个值
	 */
	public Object put(Object key, Object value) {
		if (value == null) {
			return data.remove(key);
		}
		return data.put(key, value);
	}

	public Object get(Object key) {
		return data.get(key);
	}

	public Set<Object> keySet() {
		return data.keySet();
	}

}
