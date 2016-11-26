package com.hsystems.lms.web.module;

import com.google.inject.servlet.ServletModule;

import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Created by naungsoe on 29/8/16.
 */
public class WebModule extends ServletModule {

  @Override
  protected void configureServlets() {
    //filter("/web/*").through(AuthenticationFilter.class);
    serve("/webapi/*").with(GuiceContainer.class);
  }
}
