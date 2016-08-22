package com.catorv.gallop.job.schedule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedule {
	String value() default "";
	String cron() default "";
	String group() default "default";
	String desc() default "";
	boolean autoLock() default true;
}
