package com.hsystems.lms.school;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseClientProvider;
import com.hsystems.lms.school.repository.SchoolRepository;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRepository;
import com.hsystems.lms.school.service.SchoolService;
import com.hsystems.lms.solr.SolrClient;
import com.hsystems.lms.solr.SolrClientProvider;

/**
 * Created by naungsoe on 21/8/16.
 */
public class SchoolModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(HBaseClient.class).toProvider(HBaseClientProvider.class)
        .in(Singleton.class);
    bind(SolrClient.class).toProvider(SolrClientProvider.class)
        .in(Singleton.class);
    bind(SchoolRepository.class).to(HBaseSchoolRepository.class)
        .in(Singleton.class);
    bind(SchoolService.class).in(Singleton.class);
  }
}