package com.hsystems.lms.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import com.hsystems.lms.service.module.DefaultServiceModule;
import com.hsystems.lms.web.module.WebAPIModule;
import com.hsystems.lms.web.module.WebModule;

/**
 * Created by naungsoe on 9/8/16.
 */
public class AppContextListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(
        new DefaultServiceModule(),
        new WebAPIModule(),
        new WebModule());
  }
}
