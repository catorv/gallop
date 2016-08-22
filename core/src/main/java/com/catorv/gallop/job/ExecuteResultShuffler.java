package com.catorv.gallop.job;

import java.util.List;

public interface ExecuteResultShuffler<I, O> {
	List<O> shuffle(List<ExecuteResult<I>> results);
}
