package com.hbase.jersey;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;

/**
 * Created by administrator on 27/10/16.
 */
public class ServletModule extends JerseyServletModule {

  @Override
  protected void configureServlets() {
    HashMap<String, String> options = new HashMap<>();
    options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
    serve("/webapi/*").with(GuiceContainer.class, options);
  }
}
