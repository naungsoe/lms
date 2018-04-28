package com.hsystems.lms.module;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

import com.hsystems.lms.web.AuthenticationFilter;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by naungsoe on 29/8/16.
 */
public final class WebModule extends ServletModule {

  @Override
  protected void configureServlets() {
    //filter("/web/*").through(AuthenticationFilter.class);
    serve("/webapi/*").with(GuiceContainer.class);
  }
}
