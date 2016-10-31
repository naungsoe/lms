package com.hsystems.lms.provider.solr;

import com.google.inject.Provider;

import com.hsystems.lms.ReflectionUtils;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by naungsoe on 25/9/16.
 */
public final class SolrClient {

  private Provider<Properties> propertiesProvider;

  SolrClient() {

  }

  SolrClient(Provider<Properties> propertiesProvider) {
    this.propertiesProvider = propertiesProvider;
  }

  public <T> List<T> query(SolrQuery query, Class<T> type)
    throws SolrServerException, IOException,
    InstantiationException, IllegalAccessException,
    InvocationTargetException {

    CloudSolrClient client = getClient();
    QueryResponse response = client.query(query);
    SolrDocumentList documents = response.getResults();
    return getModels(documents, type);
  }

  protected <T> List<T> getModels(
      SolrDocumentList documents, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException {

    List<T> models = new ArrayList<T>();

    for (SolrDocument document : documents) {
      T model = (T) ReflectionUtils.getInstance(type);
      populateModel(model, document);
      models.add(model);
    }

    return models;
  }

  protected <T> void populateModel(T model, SolrDocument document) {
    Field[] fields = model.getClass().getDeclaredFields();

    for (Field field : fields) {
      if (field.getType() == List.class) {
        populateList(model, field, document);
      } else {
        populateItem(model, field, document);
      }
    }
  }

  protected <T> void populateList(
      T model, Field field, SolrDocument document) {

  }

  protected <T> void populateItem(
      T model, Field field, SolrDocument document) {

  }

  public <T> void index(T model)
      throws SolrServerException, IOException,
      NoSuchFieldException, IllegalAccessException {

    SolrInputDocument document = new SolrInputDocument();
    populateDocument(document, model);

    CloudSolrClient cloudSolrClient = getClient();
    cloudSolrClient.add(document);
    cloudSolrClient.commit();
  }

  protected <T> void populateDocument(
      SolrInputDocument document, T model)
      throws SolrServerException, IOException,
      NoSuchFieldException, IllegalAccessException {

    Field[] fields = model.getClass().getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);
      document.addField(field.getName(),
          ReflectionUtils.getString(model, field.getName()));
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
