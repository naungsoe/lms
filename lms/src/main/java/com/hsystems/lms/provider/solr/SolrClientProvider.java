package com.hsystems.lms.provider.solr;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Properties;

/**
 * Created by administrator on 19/9/16.
 */
public class SolrClientProvider implements Provider<SolrClient> {

  private Provider<Properties> propertiesProvider;

  @Inject
  SolrClientProvider(Provider<Properties> propertiesProvider) {
    this.propertiesProvider = propertiesProvider;
  }

  public SolrClient get() {
    return new SolrClient(propertiesProvider);
  }
}
