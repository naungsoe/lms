package com.hsystems.lms.repository.solr.provider;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;

import org.apache.commons.lang3.StringUtils;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by naungsoe on 25/9/16.
 */
public class SolrClient {

  private static final String FIELD_ID = "id";
  private static final String FIELD_TYPE_NAME = "typeName_s";

  private static final String SEPARATOR_ID = "_";

  private Properties properties;

  SolrClient() {

  }

  SolrClient(Properties properties) {
    this.properties = properties;
  }

  public <T> QueryResult<T> query(Query query, Class<T> type)
      throws IOException {

    try {
      CloudSolrClient client = getClient(type);
      SolrQuery solrQuery = getSolrQuery(query, type);
      QueryResponse response = client.query(solrQuery);

      return new QueryResult<T>(
          response.getElapsedTime(),
          getEntities(response.getResults(), type)
      );
    } catch (SolrServerException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {

      throw new IOException(
          "error querying entities", e);
    }
  }

  protected <T> CloudSolrClient getClient(Class<T> type) {
    IndexCollection annotation = type.getAnnotation(IndexCollection.class);
    String collection = StringUtils.isEmpty(annotation.name())
        ? type.getSimpleName() : annotation.name();

    String zkHost = properties.getProperty("app.zookeeper.quorum")
        + ':' + properties.getProperty("app.zookeeper.client.port");
    CloudSolrClient client = new CloudSolrClient.Builder()
        .withZkHost(zkHost).build();
    client.setDefaultCollection(collection);
    return client;
  }

  private <T> SolrQuery getSolrQuery(Query query, Class<T> type) {
    SolrQuery solrQuery = new SolrQuery();
    solrQuery.setQuery("*:*");

    List<Criterion> criteria = query.getCriteria();
    criteria.stream().forEach(x -> {
      switch (x.getOperator()) {
        case EQUAL:
          solrQuery.addFilterQuery(String.format("%s:%s",
              x.getField(), x.getValue()));
          break;

        default:
          solrQuery.addFilterQuery(String.format("%s:%s",
              x.getField(), x.getValue()));
          break;
      }
    });

    String typeNameFilterQuery = String.format("%s:%s",
        FIELD_TYPE_NAME, type.getTypeName());
    solrQuery.addFilterQuery(typeNameFilterQuery);

    List<String> fields = query.getFields();

    if (fields.isEmpty()) {
      solrQuery.addField("*");

    } else {
      fields.stream().forEach(x -> solrQuery.addField(x));
    }

    String typeNameField = String.format("[child parentFilter=%s:%s]",
        FIELD_TYPE_NAME, type.getTypeName());
    solrQuery.addField(typeNameField);

    solrQuery.setStart(query.getOffset());
    return solrQuery;
  }

  protected <T> List<T> getEntities(SolrDocumentList documents, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    if (documents.isEmpty()) {
      Collections.emptyList();
    }

    List<T> entities = new ArrayList<>();

    for (SolrDocument document : documents) {
      if (type.getTypeName().equals(
          document.getFieldValue(FIELD_TYPE_NAME))) {
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

      populateProperty(entity, document, field);
    }

    return entity;
  }

  protected <T> void populateProperty(
      T entity, SolrDocument document, Field field)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    IndexField annotation = field.getAnnotation(IndexField.class);
    String fieldName = StringUtils.isEmpty(annotation.name())
        ? field.getName() : annotation.name();
    Object childEntity;

    switch (annotation.type()) {
      case IDENTITY:
        String id = document.getFieldValue(fieldName).toString();

        if (id.contains(SEPARATOR_ID)) {
          ReflectionUtils.setValue(entity, fieldName,
              id.substring(id.lastIndexOf(SEPARATOR_ID)));

        } else {
          ReflectionUtils.setValue(entity, fieldName, id);
        }
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
        Object datetime = document.getFieldValue(fieldName + "_dt");

        if (datetime != null) {
          ReflectionUtils.setValue(entity, fieldName,
              DateTimeUtils.toLocalDateTime((Date) datetime));
        }
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
        List<String> textGenerals
            = (List<String>) document.getFieldValue(fieldName + "_t");
        ReflectionUtils.setValue(entity, fieldName, textGenerals.get(0));
        break;

      case TEXT_WHITE_SPACE:
        ReflectionUtils.setValue(entity, fieldName,
            document.getFieldValue(fieldName + "_ws"));
        break;

      case OBJECT:
        if (document.getChildDocuments() == null) {
          ReflectionUtils.setValue(entity, fieldName, null);

        } else {
          Optional<SolrDocument> documentOptional
              = document.getChildDocuments().stream()
              .filter(x -> fieldName.equals(x.getFieldValue("fieldName_s")))
              .findFirst();

          if (documentOptional.isPresent()) {
            childEntity = getEntity(documentOptional.get(), field.getType());
            ReflectionUtils.setValue(entity, fieldName, childEntity);
          }
        }
        break;

      case LIST:
        if (document.getChildDocuments() == null) {
          ReflectionUtils.setValue(entity, fieldName, Collections.emptyList());

        } else {
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
        }
        break;
    }
  }

  public <T> void index(T entity)
      throws IOException {

    try {
      CloudSolrClient client = getClient(entity.getClass());
      SolrInputDocument document = getDocument(entity);
      updateChildDocumentsId(document);
      client.add(document);
      client.commit();

    } catch (NoSuchFieldException | IllegalAccessException
        | SolrServerException e) {

      throw new IOException(
          "error indexing entity", e);
    }
  }

  protected <T> SolrInputDocument getDocument(T entity)
      throws IllegalAccessException, NoSuchFieldException {

    SolrInputDocument document = new SolrInputDocument();
    List<Field> fields = ReflectionUtils.getFields(entity.getClass());

    for (Field field : fields) {
      if (!field.isAnnotationPresent(IndexField.class)) {
        continue;
      }

      populateFields(document, entity, field);
    }

    document.addField(FIELD_TYPE_NAME, entity.getClass().getTypeName());
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
    String fieldName = StringUtils.isEmpty(annotation.name())
        ? field.getName() : annotation.name();
    SolrInputDocument childDocument;

    switch (annotation.type()) {
      case IDENTITY:
        document.addField(FIELD_ID, fieldValue);
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

  protected void updateChildDocumentsId(SolrInputDocument document) {
    Object documentId = document.getFieldValue(FIELD_ID);

    document.getChildDocuments().forEach(x -> {
      Object childDocumentId = x.getFieldValue(FIELD_ID);
      x.setField(FIELD_ID, documentId.toString() + SEPARATOR_ID
          + childDocumentId.toString());
    });
  }

  public <T> void delete(T entity)
      throws IOException {


  }
}
