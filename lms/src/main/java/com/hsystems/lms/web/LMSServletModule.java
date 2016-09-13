package com.hsystems.lms.web;

import com.google.inject.servlet.GuiceFilter;

import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.webapi.UserController;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 29/8/16.
 */
public class LMSServletModule extends JerseyServletModule {

  @Override
  protected void configureServlets() {
    filter("/web/*").through(AuthenticationFilter.class);
    serve("/webapi/*").with(GuiceContainer.class);
  }
}
