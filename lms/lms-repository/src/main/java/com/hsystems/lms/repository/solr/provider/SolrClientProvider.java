package com.hsystems.lms.repository.solr.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Properties;

/**
 * Created by naungsoe on 19/9/16.
 */
public class SolrClientProvider implements Provider<SolrClient> {

  private final Provider<Properties> propertiesProvider;

  private volatile SolrClient client;

  @Inject
  SolrClientProvider(Provider<Properties> propertiesProvider) {
    this.propertiesProvider = propertiesProvider;
  }

  public SolrClient get() {
    SolrClient instance = client;

    if (instance == null) {
      synchronized (this) {
        instance = client;

        if (instance == null) {
          client = new SolrClient(propertiesProvider);
          instance = client;
        }
      }
    }

    return instance;
  }
}
