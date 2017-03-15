package com.hsystems.lms.common.annotation;

import com.hsystems.lms.common.LoggerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by naungsoe on 14/9/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {

  LoggerType value() default LoggerType.ROOT;
}
