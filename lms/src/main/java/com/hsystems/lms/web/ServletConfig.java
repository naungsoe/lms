package com.hsystems.lms.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.domain.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.rest.UserController;
import com.hsystems.lms.service.SearchService;
import com.hsystems.lms.service.solr.SolrSearchService;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Created by administrator on 9/8/16.
 */
public class ServletConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {

    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        super.configureServlets();

        bind(SearchService.class).to(SolrSearchService.class);
        bind(UserRepository.class).to(HBaseUserRepository.class);
        bind(UserController.class);

        serve("/*").with(HomeServlet.class);
        serve("/users*").with(UserServlet.class);
        serve("/webapi/*").with(GuiceContainer.class);
      }
    });
  }
}
