package com.hsystems.lms.module;

import com.google.inject.AbstractModule;

import com.hsystems.lms.indexer.UserIndexer;
import com.hsystems.lms.indexer.solr.SolrUserIndexer;
import com.hsystems.lms.provider.PropertiesProvider;
import com.hsystems.lms.provider.solr.SolrClient;
import com.hsystems.lms.provider.solr.SolrClientProvider;

import java.util.Properties;

/**
 * Created by administrator on 3/10/16.
 */
public class AppModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Properties.class).toProvider(PropertiesProvider.class);
    bind(SolrClient.class).toProvider(SolrClientProvider.class);
    bind(UserIndexer.class).to(SolrUserIndexer.class);
  }
}
