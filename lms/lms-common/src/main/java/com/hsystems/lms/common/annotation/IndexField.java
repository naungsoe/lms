package com.hsystems.lms.common.annotation;

import com.hsystems.lms.common.IndexFieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by naungsoe on 17/9/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface IndexField {

  String name() default "";

  IndexFieldType type() default IndexFieldType.STRING;
}