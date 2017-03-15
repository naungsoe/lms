package com.hsystems.lms.repository.solr.provider;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.common.util.StringUtils;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by naungsoe on 25/9/16.
 */
public class SolrClient {

  private static final String FIELD_ALL = "*";
  private static final String FIELD_ID = "id";
  private static final String FIELD_TYPE_NAME = "typeName_s";

  private static final String FILTER_FORMAT = "%s:%s";

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
      SolrQuery solrQuery = getSolrQuery(query, type);
      CloudSolrClient client = getClient(type);
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
    String zkHost = properties.getProperty("app.zookeeper.quorum")
        + ':' + properties.getProperty("app.zookeeper.client.port");
    CloudSolrClient client = new CloudSolrClient.Builder()
        .withZkHost(zkHost).build();

    IndexCollection annotation = type.getAnnotation(IndexCollection.class);
    String collection = StringUtils.isEmpty(annotation.name())
        ? type.getSimpleName() : annotation.name();
    client.setDefaultCollection(collection);
    return client;
  }

  private <T> SolrQuery getSolrQuery(Query query, Class<T> type)
      throws NoSuchFieldException {

    SolrQuery solrQuery = new SolrQuery();
    solrQuery.setQuery("*:*");

    updateCriteriaFieldName(query, type);
    addQueryCriteria(solrQuery, query.getCriteria());

    String typeNameFilterQuery = String.format(FILTER_FORMAT,
        FIELD_TYPE_NAME, type.getTypeName());
    solrQuery.addFilterQuery(typeNameFilterQuery);

    addQueryFields(solrQuery, query.getFields());

    String typeNameField = String.format("[child parentFilter=%s:%s]",
        FIELD_TYPE_NAME, type.getTypeName());
    solrQuery.addField(typeNameField);

    solrQuery.setStart(query.getOffset());
    solrQuery.setRows(query.getLimit());
    return solrQuery;
  }

  private <T> void updateCriteriaFieldName(Query query, Class<T> type)
      throws NoSuchFieldException {

    for (Criterion criterion : query.getCriteria()) {
      Optional<Field> fieldOptional
          = ReflectionUtils.getField(type, criterion.getField());

      if (fieldOptional.isPresent()) {
        Field field = fieldOptional.get();
        IndexField annotation = field.getAnnotation(IndexField.class);
        String fieldName = StringUtils.isEmpty(annotation.name())
            ? field.getName() : annotation.name();

        switch (annotation.type()) {
          case IDENTITY:
            // Nothing to update
            break;
          default:
            criterion.setField(fieldName + "_s");
            break;
        }
      }
    }
  }

  private void addQueryCriteria(
      SolrQuery solrQuery, List<Criterion> criteria) {

    criteria.forEach(criterion -> {
      switch (criterion.getOperator()) {
        case EQUAL:
          solrQuery.addFilterQuery(String.format(FILTER_FORMAT,
              criterion.getField(), criterion.getValue()));
          break;
        default:
          solrQuery.addFilterQuery(String.format(FILTER_FORMAT,
              criterion.getField(), criterion.getValue()));
          break;
      }
    });
  }

  private void addQueryFields(
      SolrQuery solrQuery, List<String> fields) {

    if (fields.isEmpty()) {
      solrQuery.addField(FIELD_ALL);

    } else {
      fields.forEach(solrQuery::addField);
    }
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
      if (isEntityDocument(document, type)) {
        entities.add(getEntity(document, type));
      }
    }

    return entities;
  }

  private <T> boolean isEntityDocument(
      SolrDocument document, Class<T> type) {

    Object value = document.getFieldValue(FIELD_TYPE_NAME);
    return type.getTypeName().equals(value.toString());
  }

  protected <T> T getEntity(SolrDocument document, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    T entity = (T) ReflectionUtils.getInstance(type);
    List<Field> fields = ReflectionUtils.getFields(entity.getClass());

    for (Field field : fields) {
      if (field.isAnnotationPresent(IndexField.class)) {
        populateProperty(entity, document, field);
      }
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
          ReflectionUtils.setValue(entity, field.getName(),
              id.substring(id.lastIndexOf(SEPARATOR_ID) + 1));

        } else {
          ReflectionUtils.setValue(entity, field.getName(), id);
        }
        break;
      case BOOLEAN:
        ReflectionUtils.setValue(entity, field.getName(),
            document.getFieldValue(fieldName + "_b"));
        break;
      case INTEGER:
        ReflectionUtils.setValue(entity, field.getName(),
            document.getFieldValue(fieldName + "_i"));
        break;
      case LONG:
        ReflectionUtils.setValue(entity, field.getName(),
            document.getFieldValue(fieldName + "_l"));
        break;
      case FLOAT:
        ReflectionUtils.setValue(entity, field.getName(),
            document.getFieldValue(fieldName + "_f"));
        break;
      case DOUBLE:
        ReflectionUtils.setValue(entity, field.getName(),
            document.getFieldValue(fieldName + "_d"));
        break;
      case DATETIME:
        Object datetime = document.getFieldValue(fieldName + "_dt");

        if (datetime != null) {
          ReflectionUtils.setValue(entity, field.getName(),
              DateTimeUtils.toLocalDateTime((Date) datetime));
        }
        break;
      case STRING:
        Object value = document.getFieldValue(fieldName + "_s");

        if (field.getType().isEnum()) {
          Class<Enum> enumType = (Class<Enum>) field.getType();
          ReflectionUtils.setValue(entity, field.getName(),
              Enum.valueOf(enumType, value.toString()));

        } else {
          ReflectionUtils.setValue(entity, field.getName(), value);
        }
        break;
      case TEXT_GENERAL:
        List<String> textGenerals
            = (List<String>) document.getFieldValue(fieldName + "_t");
        ReflectionUtils.setValue(entity, field.getName(), textGenerals.get(0));
        break;
      case TEXT_WHITE_SPACE:
        ReflectionUtils.setValue(entity, field.getName(),
            document.getFieldValue(fieldName + "_ws"));
        break;
      case OBJECT:
        if (document.getChildDocuments() == null) {
          ReflectionUtils.setValue(entity, field.getName(), null);

        } else {
          Optional<SolrDocument> documentOptional
              = document.getChildDocuments().stream()
              .filter(isChildDocument(fieldName)).findFirst();

          if (documentOptional.isPresent()) {
            childEntity = getEntity(documentOptional.get(), field.getType());
            ReflectionUtils.setValue(entity, field.getName(), childEntity);
          }
        }
        break;
      case LIST:
        if (ReflectionUtils.getListType(field).isEnum()) {
          Object childValue = document.getFieldValue(fieldName + "_s");

          if (childValue == null) {
            ReflectionUtils.setValue(entity,
                fieldName, Collections.emptyList());

          } else {
            List<Enum> childEnums = new ArrayList<>();
            Class<Enum> listType
                = (Class<Enum>) ReflectionUtils.getListType(field);
            String[] childValues = childValue.toString().split(",");
            Arrays.asList(childValues).forEach(
                enumValue -> childEnums.add(Enum.valueOf(listType, enumValue)));
            ReflectionUtils.setValue(entity, fieldName, childEnums);
          }
        } else if (document.getChildDocuments() == null) {
          ReflectionUtils.setValue(entity,
              fieldName, Collections.emptyList());

        } else {
          List<Object> childEntities = new ArrayList<>();
          Class<?> listType = ReflectionUtils.getListType(field);
          List<SolrDocument> childDocuments
              = document.getChildDocuments().stream()
              .filter(isChildDocument(fieldName))
              .collect(Collectors.toList());

          for (SolrDocument childDocument : childDocuments) {
            childEntity = getEntity(childDocument, listType);
            childEntities.add(childEntity);
          }

          ReflectionUtils.setValue(entity, field.getName(), childEntities);
        }
        break;
      default:
        ReflectionUtils.setValue(entity, field.getName(),
            document.getFieldValue(fieldName + "_s"));
        break;
    }
  }

  private Predicate<SolrDocument> isChildDocument(String name) {
    return document -> name.equals(document.getFieldValue("fieldName_s"));
  }

  public <T> void index(T entity)
      throws IOException {

    try {
      SolrInputDocument document = getDocument(entity);
      updateDocumentType(document, entity);
      updateChildDocumentsId(document);

      CloudSolrClient client = getClient(entity.getClass());
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
      if (field.isAnnotationPresent(IndexField.class)) {
        populateFields(document, entity, field);
      }
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
        if (ReflectionUtils.getListType(field).isEnum()) {
          document.addField(fieldName + "_s",
              StringUtils.join((List) fieldValue, ","));

        } else {
          List<SolrInputDocument> childDocuments = new ArrayList<>();

          for (Object item : (List) fieldValue) {
            childDocument = getDocument(item);
            childDocument.addField("fieldName_s", fieldName);
            childDocuments.add(childDocument);
          }

          document.addChildDocuments(childDocuments);
        }
        break;
      default:
        document.addField(fieldName + "_s", fieldValue);
        break;
    }
  }

  protected <T> void updateDocumentType(SolrInputDocument document, T entity) {
    document.addField(FIELD_TYPE_NAME, entity.getClass().getTypeName());
  }

  protected void updateChildDocumentsId(SolrInputDocument document) {
    Object documentId = document.getFieldValue(FIELD_ID);
    document.getChildDocuments()
        .forEach(childDocument -> {
          Object childDocumentId = childDocument.getFieldValue(FIELD_ID);
          String value = String.format("%s%s%s", documentId.toString(),
              SEPARATOR_ID, childDocumentId.toString());
          childDocument.setField(FIELD_ID, value);
        });
  }

  public <T> void delete(T entity)
      throws IOException {

    try {
      List<Field> fields = ReflectionUtils.getFields(entity.getClass());
      Optional<Field> fieldOptional = fields.stream()
          .filter(isIdentityField()).findFirst();

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

  private Predicate<Field> isIdentityField() {
    return field -> field.isAnnotationPresent(IndexField.class)
        && field.getAnnotation(IndexField.class).annotationType()
        .equals(IndexFieldType.IDENTITY);
  }
}
