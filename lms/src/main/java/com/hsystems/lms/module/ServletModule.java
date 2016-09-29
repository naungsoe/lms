package com.hsystems.lms.module;

import com.hsystems.lms.web.AuthenticationFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Created by administrator on 29/8/16.
 */
public class ServletModule extends JerseyServletModule {

  @Override
  protected void configureServlets() {
    filter("/jsp/*", "/web/*").through(AuthenticationFilter.class);
    serve("/webapi/*").with(GuiceContainer.class);
  }
}
