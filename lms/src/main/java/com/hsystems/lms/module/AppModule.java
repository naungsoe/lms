package com.hsystems.lms.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.hsystems.lms.provider.PropertiesProvider;
import com.hsystems.lms.provider.hbase.HBaseClient;
import com.hsystems.lms.provider.hbase.HBaseClientProvider;
import com.hsystems.lms.provider.solr.SolrClient;
import com.hsystems.lms.provider.solr.SolrClientProvider;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.hbase.HBaseGroupRepository;
import com.hsystems.lms.repository.hbase.HBaseSchoolRepository;
import com.hsystems.lms.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.IndexingService;
import com.hsystems.lms.service.SchoolService;
import com.hsystems.lms.service.SearchService;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.solr.SolrIndexingService;
import com.hsystems.lms.service.solr.SolrSearchService;
import com.hsystems.lms.webapi.AccountController;
import com.hsystems.lms.webapi.SchoolController;
import com.hsystems.lms.webapi.UserController;

import java.util.Properties;

/**
 * Created by naungsoe on 21/8/16.
 */
public final class AppModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Properties.class).toProvider(PropertiesProvider.class);
    bind(HBaseClient.class).toProvider(HBaseClientProvider.class);
    bind(SolrClient.class).toProvider(SolrClientProvider.class);
    bind(IndexingService.class).to(SolrIndexingService.class);
    bind(SearchService.class).to(SolrSearchService.class);
    bind(SchoolRepository.class).to(HBaseSchoolRepository.class);
    bind(GroupRepository.class).to(HBaseGroupRepository.class);
    bind(UserRepository.class).to(HBaseUserRepository.class);
    bind(AuthenticationService.class);
    bind(SchoolService.class);
    bind(UserService.class);
    bind(AccountController.class);
    bind(SchoolController.class);
    bind(UserController.class);
  }
}
