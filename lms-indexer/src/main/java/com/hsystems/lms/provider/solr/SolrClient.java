package com.hsystems.lms.provider.solr;

import com.google.inject.Provider;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by administrator on 3/10/16.
 */
public final class SolrClient {

  private Provider<Properties> propertiesProvider;

  SolrClient(Provider<Properties> propertiesProvider) {
    this.propertiesProvider = propertiesProvider;
  }

  public void index(String collection, List<SolrInputDocument> documents)
      throws SolrServerException, IOException {

    CloudSolrClient client = getClient();

    try {
      client.setDefaultCollection(collection);
      client.add(documents);
      client.commit();
    } finally {
      client.close();
    }
  }

  protected CloudSolrClient getClient() {
    Properties properties = propertiesProvider.get();
    String zkHost = properties.getProperty("app.zookeeper.quorum")
        + ':' + properties.getProperty("app.zookeeper.client.port");
    return new CloudSolrClient.Builder()
        .withZkHost(zkHost).build();
  }
}
