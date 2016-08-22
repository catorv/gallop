package com.catorv.gallop.test.junit;

import com.google.inject.Module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 测试用例使用的模块
 * Created by cator on 6/20/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GuiceModule {
	Class<? extends Module>[] value();
}