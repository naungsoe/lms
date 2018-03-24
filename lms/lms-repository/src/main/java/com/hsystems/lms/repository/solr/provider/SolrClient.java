package com.hsystems.lms.repository.solr.provider;

import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.IndexDocument;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.solr.mapper.Constants;
import com.hsystems.lms.repository.solr.mapper.DocumentMapper;
import com.hsystems.lms.repository.solr.mapper.EntityMapper;
import com.hsystems.lms.repository.solr.mapper.QueryMapper;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created by naungsoe on 25/9/16.
 */
public class SolrClient {

  private static final String COLLECTION_FORMAT = "%s.%s";

  private Provider<Properties> propertiesProvider;

  private volatile CloudSolrClient cloudClient;

  SolrClient() {

  }

  SolrClient(Provider<Properties> propertiesProvider) {
    this.propertiesProvider = propertiesProvider;
  }

  public <T extends Entity> QueryResult<T> query(Query query, Class<T> type)
      throws IOException {

    try {
      String collection = getCollection(type);
      CloudSolrClient client = getCloudClient();
      client.setDefaultCollection(collection);

      QueryMapper mapper = new QueryMapper();
      SolrQuery solrQuery = mapper.map(query, type);
      QueryResponse response = client.query(solrQuery);
      SolrDocumentList results = response.getResults();
      List<T> entities = getEntities(results, type);
      return new QueryResult<>(
          response.getElapsedTime(),
          results.getStart(),
          results.getNumFound(),
          entities
      );
    } catch (SolrServerException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException | ClassNotFoundException e) {

      throw new IOException(
          "error querying entities", e);
    }
  }

  protected synchronized CloudSolrClient getCloudClient() {
    CloudSolrClient instance = cloudClient;

    if (instance == null) {
      synchronized (this) {
        instance = cloudClient;

        if (instance == null) {
          Properties properties = propertiesProvider.get();
          String zkQuorum =properties.getProperty("app.zookeeper.quorum");
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

  private <T extends Entity> String getCollection(Class<T> type) {
    IndexDocument annotation = getAnnotation(type);
    String namespace = annotation.namespace();
    namespace = StringUtils.isEmpty(namespace) ? "default" : namespace;

    String collection = annotation.collection();
    collection = StringUtils.isEmpty(collection)
        ? type.getSimpleName() : collection;
    return String.format(COLLECTION_FORMAT, namespace, collection);
  }

  private <T extends Entity> IndexDocument getAnnotation(Class<T> type) {
    IndexDocument annotation = type.getAnnotation(IndexDocument.class);

    if (annotation == null) {
      Class<?> superType = type.getInterfaces()[0];

      while (annotation == null) {
        annotation = superType.getAnnotation(IndexDocument.class);
        superType = superType.getInterfaces()[0];
      }
    }

    return annotation;
  }

  protected <T extends Entity> List<T> getEntities(
      SolrDocumentList documents, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException,
      ClassNotFoundException {

    if (CollectionUtils.isEmpty(documents)) {
      Collections.emptyList();
    }

    EntityMapper mapper = EntityMapper.getInstance();
    List<T> entities = new ArrayList<>();

    for (SolrDocument document : documents) {
      T entity = mapper.map(document, type);
      entities.add(entity);
    }

    return entities;
  }

  public <T extends Entity> void index(List<T> entities)
      throws IOException {

    try {
      String collection = getCollection(entities.get(0).getClass());
      CloudSolrClient client = getCloudClient();
      client.setDefaultCollection(collection);

      DocumentMapper mapper = DocumentMapper.getInstance();

      for (Entity entity : entities) {
        SolrInputDocument document = mapper.map(entity);
        client.add(document);
      }

      client.commit();

    } catch (NoSuchFieldException | IllegalAccessException
        | InstantiationException | InvocationTargetException
        | SolrServerException e) {

      throw new IOException(
          "error indexing entities", e);
    }
  }

  public <T extends Entity> void index(T entity)
      throws IOException {

    try {
      String collection = getCollection(entity.getClass());
      CloudSolrClient client = getCloudClient();
      client.setDefaultCollection(collection);

      DocumentMapper mapper = DocumentMapper.getInstance();
      SolrInputDocument document = mapper.map(entity);
      client.add(document);
      client.commit();

    } catch (NoSuchFieldException | IllegalAccessException
        | InstantiationException | InvocationTargetException
        | SolrServerException e) {

      throw new IOException(
          "error indexing entity", e);
    }
  }

  public <T extends Entity> void delete(List<T> entities)
      throws IOException {

    try {
      String collection = getCollection(entities.get(0).getClass());
      CloudSolrClient client = getCloudClient();

      for (Entity entity : entities) {
        client.deleteById(collection, entity.getId());
      }

      client.commit();

    } catch (SolrServerException e) {
      throw new IOException(
          "error deleting entity", e);
    }
  }

  public <T extends Entity> void delete(T entity)
      throws IOException {

    try {
      String collection = getCollection(entity.getClass());
      CloudSolrClient client = getCloudClient();
      client.setDefaultCollection(collection);
      client.deleteByQuery(String.format("%s:%s*",
          Constants.FIELD_ID, entity.getId()));
      client.commit();

    } catch (SolrServerException e) {
      throw new IOException(
          "error deleting entity", e);
    }
  }
}