package com.hsystems.lms.repository.solr.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Properties;

/**
 * Created by naungsoe on 19/9/16.
 */
public class SolrClientProvider implements Provider<SolrClient> {

  private final Properties properties;

  private final SolrClient client;

  @Inject
  SolrClientProvider(Properties properties) {
    this.properties = properties;
    this.client = new SolrClient(properties);
  }

  public SolrClient get() {
    return client;
  }
}
