package com.hsystems.lms.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import com.hsystems.lms.module.AppModule;
import com.hsystems.lms.module.LogModule;
import com.hsystems.lms.module.PermissionModule;
import com.hsystems.lms.module.WebModule;

/**
 * Created by naungsoe on 9/8/16.
 */
public class AppContextListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(
        new AppModule(),
        new LogModule(),
        new PermissionModule(),
        new WebModule());
  }
}
