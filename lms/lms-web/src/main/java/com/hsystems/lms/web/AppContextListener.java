package com.hsystems.lms.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import com.hsystems.lms.web.module.WebAPIModule;
import com.hsystems.lms.web.module.WebModule;

/**
 * Created by naungsoe on 9/8/16.
 */
public class AppContextListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(
        new com.hsystems.lms.service.module.SchoolModule(),
        new WebAPIModule(),
        new WebModule());
  }
}
