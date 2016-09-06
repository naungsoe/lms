package com.hsystems.lms.web;

import com.google.inject.AbstractModule;

import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.domain.repository.hbase.HBaseUserMapper;
import com.hsystems.lms.domain.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.webapi.UserController;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.SearchService;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.solr.SolrSearchService;

/**
 * Created by administrator on 21/8/16.
 */
public class LMSModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SearchService.class).to(SolrSearchService.class);
    bind(UserRepository.class).to(HBaseUserRepository.class);
    bind(HBaseUserMapper.class);
    bind(AuthenticationService.class);
    bind(UserService.class);
    bind(UserController.class);
  }
}
