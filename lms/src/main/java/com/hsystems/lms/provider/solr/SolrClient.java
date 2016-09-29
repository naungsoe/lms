package com.hsystems.lms.provider.solr;

import com.google.inject.Provider;

import com.hsystems.lms.MappingUtils;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by administrator on 25/9/16.
 */
public final class SolrClient {

  private Provider<Properties> propertiesProvider;

  SolrClient() {

  }

  SolrClient(Provider<Properties> propertiesProvider) {
    this.propertiesProvider = propertiesProvider;
  }

  public <T> Optional<T> query() {
    return Optional.empty();
  }

  public <T> void index(T entity)
      throws SolrServerException, IOException,
      NoSuchFieldException, IllegalAccessException {

    SolrInputDocument document = new SolrInputDocument();
    Field[] fields = entity.getClass().getDeclaredFields();
    populateDocument(entity, fields, document);

    CloudSolrClient client = getClient();
    client.add(document);
    client.commit();
  }

  protected <T> void populateDocument(
      T entity, Field[] fields, SolrInputDocument document)
      throws SolrServerException, IOException,
      NoSuchFieldException, IllegalAccessException {

    for (Field field : fields) {
      field.setAccessible(true);
      document.addField(field.getName(),
          MappingUtils.getStringField(entity, field.getName()));
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
