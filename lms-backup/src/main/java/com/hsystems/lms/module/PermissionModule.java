package com.hsystems.lms.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

import com.hsystems.lms.service.annotation.Requires;
import com.hsystems.lms.service.interceptor.RequiresInterceptor;

/**
 * Created by naungsoe on 17/9/16.
 */
public final class PermissionModule extends AbstractModule {

  protected void configure() {
    bindInterceptor(Matchers.any(),
        Matchers.annotatedWith(Requires.class),
        new RequiresInterceptor());
  }
}
