package com.hsystems.lms.web.module;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

import com.hsystems.lms.web.AuthenticationFilter;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Created by naungsoe on 29/8/16.
 */
public class WebModule extends ServletModule {

  @Override
  protected void configureServlets() {
    bind(AuthenticationFilter.class).in(Singleton.class);

    filter("/web/*").through(AuthenticationFilter.class);
    serve("/webapi/*").with(GuiceContainer.class);
  }
}
