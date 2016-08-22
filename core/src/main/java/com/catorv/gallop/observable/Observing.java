package com.catorv.gallop.observable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注该类包含Notification的监听方法(不包含@Observing的类的监听方法将不起作用)
 * Created by cator on 8/1/16.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observing {
}
