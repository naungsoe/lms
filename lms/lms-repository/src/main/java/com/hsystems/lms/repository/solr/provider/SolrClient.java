package com.hsystems.lms.repository.solr.provider;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.common.util.StringUtils;
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 25/9/16.
 */
public class SolrClient {

  private Properties properties;

  private volatile CloudSolrClient solrClient;

  SolrClient() {

  }

  SolrClient(Properties properties) {
    this.properties = properties;
  }

  public <T> QueryResult<T> query(Query query, Class<T> type)
      throws IOException {

    try {
      QueryMapper mapper = new QueryMapper(type);
      SolrQuery solrQuery = mapper.map(query);
      CloudSolrClient client = getClient(type);
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
        | NoSuchFieldException e) {

      throw new IOException(
          "error querying entities", e);
    }
  }

  protected synchronized <T> CloudSolrClient getClient(Class<T> type) {
    CloudSolrClient instance = solrClient;

    if (instance == null) {
      synchronized (this) {
        instance = solrClient;

        if (instance == null) {
          String zkHost = properties.getProperty("app.zookeeper.quorum")
              + ':' + properties.getProperty("app.zookeeper.client.port");
          solrClient = new CloudSolrClient.Builder()
              .withZkHost(zkHost).build();
          instance = solrClient;
        }
      }
    }

    String collection = getCollection(type);
    instance.setDefaultCollection(collection);
    return instance;
  }

  private <T> String getCollection(Class<T> type) {
    IndexCollection annotation = type.getAnnotation(IndexCollection.class);
    return StringUtils.isEmpty(annotation.name())
        ? type.getSimpleName() : annotation.name();
  }

  protected <T> List<T> getEntities(
      SolrDocumentList documents, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    if (documents.isEmpty()) {
      Collections.emptyList();
    }

    List<T> entities = new ArrayList<>();

    for (SolrDocument document : documents) {
      EntityMapper mapper = new EntityMapper(type);
      entities.add((T) mapper.map(document));
    }

    return entities;
  }

  public <T> void index(T entity)
      throws IOException {

    try {
      DocumentMapper mapper = new DocumentMapper();
      SolrInputDocument document = mapper.map(entity);

      CloudSolrClient client = getClient(entity.getClass());
      client.add(document);
      client.commit();

    } catch (NoSuchFieldException | IllegalAccessException
        | InstantiationException | InvocationTargetException
        | SolrServerException e) {

      throw new IOException(
          "error indexing entity", e);
    }
  }

  public <T> void delete(T entity)
      throws IOException {

    try {
      Optional<Field> fieldOptional = getIdentityField(entity);

      if (fieldOptional.isPresent()) {
        String fieldName = fieldOptional.get().getName();
        String id = ReflectionUtils.getString(entity, fieldName);

        CloudSolrClient client = getClient(entity.getClass());
        client.deleteById(id);
        client.commit();
      }
    } catch (SolrServerException e) {
      throw new IOException(
          "error deleting entity", e);
    }
  }

  private <T> Optional<Field> getIdentityField(T entity) {
    List<Field> fields = ReflectionUtils.getFields(entity.getClass());

    return fields.stream()
        .filter(field -> {
          if (field.isAnnotationPresent(IndexField.class)) {
            IndexField annotation = field.getAnnotation(IndexField.class);
            return StringUtils.isEmpty(annotation.name())
                ? field.getName().equals("id")
                : annotation.name().equals("id");
          }
          return false;
        })
        .findFirst();
  }
}
