package com.hsystems.lms.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by naungsoe on 14/9/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Field {

  FieldType type() default FieldType.String;

  String name() default "";

  boolean index() default true;

  boolean ignore() default false;
}
