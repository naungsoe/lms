package com.hsystems.lms.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.domain.repository.hbase.HBaseUserMapper;
import com.hsystems.lms.domain.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.domain.repository.mapping.DataMap;
import com.hsystems.lms.rest.UserController;
import com.hsystems.lms.service.SearchService;
import com.hsystems.lms.service.UserService;
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
        bind(HBaseUserMapper.class);
        bind(UserService.class);
        bind(UserController.class);

        filter("/*").through(AuthenticationFilter.class);
        serve("/login", "/login/").with(LoginServlet.class);
        serve("/home", "/home/").with(HomeServlet.class);
        serve("/users", "/users/").with(UserServlet.class);
        serve("/error", "/error/").with(UserServlet.class);
        serve("/webapi/*").with(GuiceContainer.class);
      }
    });
  }
}
