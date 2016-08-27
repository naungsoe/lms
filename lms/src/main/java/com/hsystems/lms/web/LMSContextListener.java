package com.hsystems.lms.web;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.domain.repository.hbase.HBaseUserMapper;
import com.hsystems.lms.domain.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.rest.UserController;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.SearchService;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.solr.SolrSearchService;

import javax.servlet.annotation.WebListener;

/**
 * Created by administrator on 9/8/16.
 */
@WebListener
public class LMSContextListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new LMSModule());
  }
}
