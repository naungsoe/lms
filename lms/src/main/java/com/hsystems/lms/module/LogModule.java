package com.hsystems.lms.module;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import com.hsystems.lms.annotation.Log;
import com.hsystems.lms.annotation.LogInterceptor;

/**
 * Created by administrator on 14/9/16.
 */
public class LogModule extends AbstractModule {
  protected void configure() {
    bindInterceptor(Matchers.any(),
        Matchers.annotatedWith(Log.class),
        new LogInterceptor());
  }
}
