package com.hsystems.lms.repository.solr.provider;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Entity;
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

  private static final String FIELD_ID = "id";

  private static final String ROUTE_FORMAT = "%s!";
  private static final String COMPOSITE_ID_FORMAT = "%s!%s";

  private Properties properties;

  private volatile CloudSolrClient cloudClient;

  SolrClient() {

  }

  SolrClient(Properties properties) {
    this.properties = properties;
  }

  public <T extends Entity> QueryResult<T> query(Query query, Class<T> type)
      throws IOException {

    try {
      String collection = getCollection(type);
      CloudSolrClient client = getCloudClient();
      client.setDefaultCollection(collection);

      String namespace = getNamespace(type);
      String route = String.format(ROUTE_FORMAT, namespace);
      query.getCriteria().forEach(criterion -> {
            if (FIELD_ID.equals(criterion.getField())) {
              List<Object> compositeIds = new ArrayList<>();
              criterion.getValues().forEach(value -> {
                String compositeId = String.format(
                    COMPOSITE_ID_FORMAT, namespace, value);
                compositeIds.add(compositeId);
              });
              criterion.setValues(compositeIds);
            }
          });

      QueryMapper mapper = new QueryMapper(type);
      SolrQuery solrQuery = mapper.map(query);
      solrQuery.set("_route_", route);

      QueryResponse response = client.query(solrQuery);
      SolrDocumentList results = response.getResults();
      results.forEach(result -> {
        String compositeId = (String) result.getFieldValue(FIELD_ID);
        String id = compositeId.substring(route.length());
        result.setField(FIELD_ID, id);
      });

      List<T> entities = getEntities(results, type);
      return new QueryResult<>(
          response.getElapsedTime(),
          results.getStart(),
          results.getNumFound(),
          entities
      );
    } catch (SolrServerException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {

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
          String zkHost = properties.getProperty("app.zookeeper.quorum")
              + ':' + properties.getProperty("app.zookeeper.client.port");
          cloudClient = new CloudSolrClient.Builder()
              .withZkHost(zkHost).build();
          instance = cloudClient;
        }
      }
    }

    return instance;
  }

  private <T extends Entity> String getNamespace(Class<T> type) {
    IndexCollection annotation = type.getAnnotation(IndexCollection.class);
    String namespace = annotation.namespace();
    return StringUtils.isEmpty(namespace) ? "default" : namespace;
  }

  private <T extends Entity> String getCollection(Class<T> type) {
    IndexCollection annotation = type.getAnnotation(IndexCollection.class);
    String collection = annotation.name();
    return StringUtils.isEmpty(collection)
        ? type.getSimpleName() : collection;
  }

  protected <T extends Entity> List<T> getEntities(
      SolrDocumentList documents, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    if (CollectionUtils.isEmpty(documents)) {
      Collections.emptyList();
    }

    List<T> entities = new ArrayList<>();

    for (SolrDocument document : documents) {
      EntityMapper mapper = new EntityMapper(type);
      entities.add((T) mapper.map(document));
    }

    return entities;
  }

  public <T extends Entity> void index(List<T> entities)
      throws IOException {

    try {
      String collection = getCollection(entities.get(0).getClass());
      CloudSolrClient client = getCloudClient();
      client.setDefaultCollection(collection);

      DocumentMapper mapper = new DocumentMapper();

      for (Entity entity : entities) {
        SolrInputDocument document = mapper.map(entity);
        updateDocumentId(document, entity);
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

  public <T extends Entity> void updateDocumentId(
      SolrInputDocument document, T entity) {

    String namespace = getNamespace(entity.getClass());
    String compositeId = String.format(
        COMPOSITE_ID_FORMAT, namespace, entity.getId());
    document.setField(FIELD_ID, compositeId);
  }

  public <T extends Entity> void index(T entity)
      throws IOException {

    try {
      String collection = getCollection(entity.getClass());
      CloudSolrClient client = getCloudClient();
      client.setDefaultCollection(collection);

      DocumentMapper mapper = new DocumentMapper();
      SolrInputDocument document = mapper.map(entity);
      updateDocumentId(document, entity);
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
      client.deleteById(entity.getId());
      client.commit();

    } catch (SolrServerException e) {
      throw new IOException(
          "error deleting entity", e);
    }
  }
}
