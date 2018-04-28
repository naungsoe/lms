package com.hsystems.lms.solr;

import com.google.inject.Provider;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by naungsoe on 25/9/16.
 */
public final class SolrClient {

  private final Provider<Properties> propertiesProvider;

  private volatile CloudSolrClient cloudClient;

  public SolrClient(Provider<Properties> propertiesProvider) {
    this.propertiesProvider = propertiesProvider;
  }

  public QueryResponse query(SolrQuery query, String collection)
      throws IOException {

    try {
      CloudSolrClient client = getCloudClient();
      client.setDefaultCollection(collection);
      QueryResponse response = client.query(query);
      return response;

    } catch (SolrServerException e) {
      throw new IOException("error querying entities", e);
    }
  }

  protected synchronized CloudSolrClient getCloudClient() {
    CloudSolrClient instance = cloudClient;

    if (instance == null) {
      synchronized (this) {
        instance = cloudClient;

        if (instance == null) {
          Properties properties = propertiesProvider.get();
          String zkQuorum = properties.getProperty("app.zookeeper.quorum");
          String zkPort = properties.getProperty("app.zookeeper.client.port");
          String zkHost = String.format("%s:%s", zkQuorum, zkPort);
          cloudClient = new CloudSolrClient.Builder()
              .withZkHost(zkHost).build();
          instance = cloudClient;
        }
      }
    }

    return instance;
  }

  public void saveAll(List<SolrInputDocument> documents, String collection)
      throws IOException {

    try {
      CloudSolrClient client = getCloudClient();
      client.setDefaultCollection(collection);

      for (SolrInputDocument document : documents) {
        client.add(document);
      }

      client.commit();

    } catch (SolrServerException e) {
      throw new IOException("error saving entities", e);
    }
  }

  public void save(SolrInputDocument document, String collection)
      throws IOException {

    try {
      CloudSolrClient client = getCloudClient();
      client.setDefaultCollection(collection);
      client.add(document);
      client.commit();

    } catch (SolrServerException e) {
      throw new IOException("error saving entity", e);
    }
  }

  public void deleteAllBy(List<String> idList, String collection)
      throws IOException {

    try {
      CloudSolrClient client = getCloudClient();

      for (String id : idList) {
        client.deleteById(collection, id);
      }

      client.commit();

    } catch (SolrServerException e) {
      throw new IOException("error deleting entity", e);
    }
  }

  public void deleteBy(String id, String collection)
      throws IOException {

    try {
      CloudSolrClient client = getCloudClient();
      client.setDefaultCollection(collection);
      String query = String.format("id:%s*", id);
      client.deleteByQuery(query);
      client.commit();

    } catch (SolrServerException e) {
      throw new IOException("error deleting entity", e);
    }
  }
}