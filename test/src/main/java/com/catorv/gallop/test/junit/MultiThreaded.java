package com.catorv.gallop.test.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by cator on 19/10/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE}) // TODO: 实现基于类方法的多线程测试机制
public @interface MultiThreaded {

	/** 线程总数 */
	int numThreads();

	/** @see java.util.concurrent.ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit, BlockingQueue) */
	int corePoolSize() default 10;

	/** @see java.util.concurrent.ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit, BlockingQueue) */
	int maximumPoolSize() default 100;

}
