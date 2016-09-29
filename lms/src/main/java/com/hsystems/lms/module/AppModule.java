package com.hsystems.lms.module;

import com.google.inject.AbstractModule;

import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.domain.repository.hbase.HBaseUserMapper;
import com.hsystems.lms.domain.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.provider.hbase.HBaseClient;
import com.hsystems.lms.provider.hbase.HBaseClientProvider;
import com.hsystems.lms.provider.solr.SolrClient;
import com.hsystems.lms.provider.solr.SolrClientProvider;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.IndexingService;
import com.hsystems.lms.service.SearchService;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.entity.UserEntity;
import com.hsystems.lms.provider.PropertiesProvider;
import com.hsystems.lms.provider.UserEntityProvider;
import com.hsystems.lms.service.solr.SolrIndexingService;
import com.hsystems.lms.service.solr.SolrSearchService;
import com.hsystems.lms.webapi.AccountController;
import com.hsystems.lms.webapi.UserController;

import java.util.Properties;

/**
 * Created by administrator on 21/8/16.
 */
public class AppModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Properties.class).toProvider(PropertiesProvider.class);
    bind(UserEntity.class).toProvider(UserEntityProvider.class);
    bind(HBaseClient.class).toProvider(HBaseClientProvider.class);
    bind(SolrClient.class).toProvider(SolrClientProvider.class);
    bind(IndexingService.class).to(SolrIndexingService.class);
    bind(SearchService.class).to(SolrSearchService.class);
    bind(UserRepository.class).to(HBaseUserRepository.class);
    bind(HBaseUserMapper.class);
    bind(AuthenticationService.class);
    bind(UserService.class);
    bind(AccountController.class);
    bind(UserController.class);
  }
}
