package com.hsystems.lms.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.domain.repository.hbase.HBaseUserMapper;
import com.hsystems.lms.domain.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.service.IndexingService;
import com.hsystems.lms.service.security.UserProvider;
import com.hsystems.lms.service.solr.SolrIndexingService;
import com.hsystems.lms.webapi.AccountController;
import com.hsystems.lms.webapi.UserController;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.SearchService;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.solr.SolrSearchService;

/**
 * Created by administrator on 21/8/16.
 */
public class AppModule extends AbstractModule {

  @Override
  protected void configure() {
    Class defaultScope = Singleton.class;
    bind(SearchService.class).to(SolrSearchService.class).in(defaultScope);
    bind(IndexingService.class).to(SolrIndexingService.class).in(defaultScope);
    bind(UserRepository.class).to(HBaseUserRepository.class).in(defaultScope);
    bind(HBaseUserMapper.class).in(defaultScope);
    bind(UserProvider.class).in(defaultScope);
    bind(AuthenticationService.class).in(defaultScope);
    bind(UserService.class).in(defaultScope);
    bind(AccountController.class).in(defaultScope);
    bind(UserController.class).in(defaultScope);
  }
}
