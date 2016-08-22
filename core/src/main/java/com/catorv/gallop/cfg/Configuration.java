package com.catorv.gallop.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置
 * Created by cator on 6/21/16.
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

	String value() default "";

	String section() default "";

	Class<?> groupType() default Object.class;

}
