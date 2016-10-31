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
public class WebModule extends ServletModule {

  @Override
  protected void configureServlets() {
    filter("/jsp/*", "/web/*").through(AuthenticationFilter.class);

    Map<String, String> params = new HashMap<>();
    params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
    serve("/webapi/*").with(GuiceContainer.class, params);
  }
}
