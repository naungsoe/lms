package com.hsystems.lms.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

import com.hsystems.lms.service.annotation.Log;
import com.hsystems.lms.service.interceptor.LogInterceptor;

/**
 * Created by naungsoe on 14/9/16.
 */
public final class LogModule extends AbstractModule {

  protected void configure() {
    bindInterceptor(Matchers.any(),
        Matchers.annotatedWith(Log.class),
        new LogInterceptor());
  }
}
