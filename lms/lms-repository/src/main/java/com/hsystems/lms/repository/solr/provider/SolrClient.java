package com.hsystems.lms.repository.solr.provider;

import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;

import org.apache.commons.lang.StringUtils;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by naungsoe on 25/9/16.
 */
public class SolrClient {

  private Provider<Properties> propertiesProvider;

  SolrClient() {

  }

  SolrClient(Provider<Properties> propertiesProvider) {
    this.propertiesProvider = propertiesProvider;
  }

  public <T> QueryResult<T> query(SolrQuery query, Class<T> type)
      throws SolrServerException, IOException,
      InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    IndexCollection annotation = type.getAnnotation(IndexCollection.class);
    String collection = StringUtils.isEmpty(annotation.value())
        ? type.getSimpleName() : annotation.value();

    CloudSolrClient client = getClient();
    client.setDefaultCollection(collection);
    QueryResponse response = client.query(query);
    return new QueryResult<T>(
        response.getElapsedTime(),
        getEntities(response.getResults(), type)
    );
  }

  protected CloudSolrClient getClient() {
    Properties properties = propertiesProvider.get();
    String zkHost = properties.getProperty("app.zookeeper.quorum")
        + ':' + properties.getProperty("app.zookeeper.client.port");
    return new CloudSolrClient.Builder()
        .withZkHost(zkHost).build();
  }

  protected <T> List<T> getEntities(SolrDocumentList documents, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    List<T> entities = new ArrayList<>();
    String typeName = type.getTypeName();

    for (SolrDocument document : documents) {
      if (typeName.equals(document.getFieldValue("typeName_s"))) {
        entities.add(getEntity(document, type));
      }
    }

    return entities;
  }

  protected <T> T getEntity(SolrDocument document, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    T entity = (T) ReflectionUtils.getInstance(type);
    List<Field> fields = ReflectionUtils.getFields(entity.getClass());

    for (Field field : fields) {
      if (!field.isAnnotationPresent(IndexField.class)) {
        continue;
      }

      populateProperties(entity, document, field);
    }

    return entity;
  }

  protected <T> void populateProperties(
      T entity, SolrDocument document, Field field)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    IndexField annotation = field.getAnnotation(IndexField.class);
    String fieldName = StringUtils.isEmpty(annotation.value())
        ? field.getName() : annotation.value();
    Object childEntity;

    switch (annotation.type()) {
      case IDENTITY:
        ReflectionUtils.setValue(entity, fieldName,
            document.getFieldValue(fieldName));
        break;

      case BOOLEAN:
        ReflectionUtils.setValue(entity, fieldName,
            document.getFieldValue(fieldName + "_b"));
        break;

      case INTEGER:
        ReflectionUtils.setValue(entity, fieldName,
            document.getFieldValue(fieldName + "_i"));
        break;

      case LONG:
        ReflectionUtils.setValue(entity, fieldName,
            document.getFieldValue(fieldName + "_l"));
        break;

      case FLOAT:
        ReflectionUtils.setValue(entity, fieldName,
            document.getFieldValue(fieldName + "_f"));
        break;

      case DOUBLE:
        ReflectionUtils.setValue(entity, fieldName,
            document.getFieldValue(fieldName + "_d"));
        break;

      case DATETIME:
        ReflectionUtils.setValue(entity, fieldName,
            document.getFieldValue(fieldName + "_dt"));
        break;

      case STRING:
        Object value = document.getFieldValue(fieldName + "_s");

        if (field.getType().isEnum()) {
          ReflectionUtils.setValue(entity, fieldName,
              Enum.valueOf((Class<Enum>) field.getType(), value.toString()));

        } else {
          ReflectionUtils.setValue(entity, fieldName, value);
        }
        break;

      case TEXT_GENERAL:
        ReflectionUtils.setValue(entity, fieldName,
            document.getFieldValue(fieldName + "_t"));
        break;

      case TEXT_WHITE_SPACE:
        ReflectionUtils.setValue(entity, fieldName,
            document.getFieldValue(fieldName + "_ws"));
        break;

      case OBJECT:
        Optional<SolrDocument> documentOptional
            = document.getChildDocuments().stream()
            .filter(x -> fieldName.equals(x.getFieldValue("fieldName_s")))
            .findFirst();

        if (documentOptional.isPresent()) {
          childEntity = getEntity(documentOptional.get(), field.getType());
          ReflectionUtils.setValue(entity, fieldName, childEntity);
        }
        break;

      case LIST:
        List<Object> childEntities = new ArrayList<>();
        Class<?> listType = ReflectionUtils.getListType(field);
        List<SolrDocument> childDocuments
            = document.getChildDocuments().stream()
            .filter(x -> fieldName.equals(x.getFieldValue("fieldName_s")))
            .collect(Collectors.toList());

        for (SolrDocument childDocument : childDocuments) {
          childEntity = getEntity(childDocument, listType);
          childEntities.add(childEntity);
        }

        ReflectionUtils.setValue(entity, fieldName, childEntities);
        break;
    }
  }

  public <T> void index(T entity)
      throws SolrServerException, IOException,
      NoSuchFieldException, IllegalAccessException {

    IndexCollection annotation = entity.getClass()
        .getAnnotation(IndexCollection.class);
    String collection = StringUtils.isEmpty(annotation.value())
        ? entity.getClass().getSimpleName() : annotation.value();

    CloudSolrClient client = getClient();
    client.setDefaultCollection(collection);
    client.add(getDocument(entity));
    client.commit();
  }

  protected <T> SolrInputDocument getDocument(T entity)
      throws IllegalAccessException, NoSuchFieldException {

    SolrInputDocument document = new SolrInputDocument();
    document.addField("typeName_s", entity.getClass().getTypeName());

    List<Field> fields = ReflectionUtils.getFields(entity.getClass());

    for (Field field : fields) {
      if (!field.isAnnotationPresent(IndexField.class)) {
        continue;
      }

      populateFields(document, entity, field);
    }

    return document;
  }

  protected <T> void populateFields(
      SolrInputDocument document, T entity, Field field)
      throws IllegalAccessException, NoSuchFieldException {

    Object fieldValue = ReflectionUtils.getValue(
        entity, field, Object.class);

    if (fieldValue == null) {
      return;
    }

    IndexField annotation = field.getAnnotation(IndexField.class);
    String fieldName = StringUtils.isEmpty(annotation.value())
        ? field.getName() : annotation.value();
    SolrInputDocument childDocument;

    switch (annotation.type()) {
      case IDENTITY:
        document.addField(fieldName, fieldValue);
        break;

      case BOOLEAN:
        document.addField(fieldName + "_b", fieldValue);
        break;

      case INTEGER:
        document.addField(fieldName + "_i", fieldValue);
        break;

      case LONG:
        document.addField(fieldName + "_l", fieldValue);
        break;

      case FLOAT:
        document.addField(fieldName + "_f", fieldValue);
        break;

      case DOUBLE:
        document.addField(fieldName + "_d", fieldValue);
        break;

      case DATETIME:
        String dateTime = DateTimeUtils.toString((LocalDateTime) fieldValue);
        document.addField(fieldName + "_dt", dateTime + "Z");
        break;

      case STRING:
        if (field.getType().isEnum()) {
          document.addField(fieldName + "_s", fieldValue.toString());

        } else {
          document.addField(fieldName + "_s", fieldValue);
        }
        break;

      case TEXT_GENERAL:
        document.addField(fieldName + "_t", fieldValue);
        break;

      case TEXT_WHITE_SPACE:
        document.addField(fieldName + "_ws", fieldValue);
        break;

      case OBJECT:
        childDocument = getDocument(fieldValue);
        childDocument.addField("fieldName_s", fieldName);
        document.addChildDocument(childDocument);
        break;

      case LIST:
        List<SolrInputDocument> childDocuments = new ArrayList<>();

        for (Object item : (List) fieldValue) {
          childDocument = getDocument(item);
          childDocument.addField("fieldName_s", fieldName);
          childDocuments.add(childDocument);
        }

        document.addChildDocuments(childDocuments);
        break;
    }
  }
}
