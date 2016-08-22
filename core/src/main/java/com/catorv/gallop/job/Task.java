package com.catorv.gallop.job;

/**
 * Task
 * Created by cator on 8/9/16.
 */
public interface Task<P, R> extends Executable<P, R> {

	String getId();

}
