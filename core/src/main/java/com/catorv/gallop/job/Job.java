package com.catorv.gallop.job;

/**
 * Job
 * Created by cator on 8/9/16.
 */
public interface Job<P, R> extends Executable<P, R>{

	String getId();

}

