package com.catorv.gallop.job;

/**
 * Executable
 * Created by cator on 8/9/16.
 */
public interface Executable<P, R> {

	R execute(P input) throws Exception;

}

