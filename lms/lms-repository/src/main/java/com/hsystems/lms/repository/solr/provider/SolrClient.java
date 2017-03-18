package com.hsystems.lms.repository.solr.provider;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ListUtils;
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
import java.util.regex.Pattern;

/**
 * Created by naungsoe on 25/9/16.
 */
public class SolrClient {

  private static final String FIELD_ALL = "*";
  private static final String FIELD_ID = "id";
  private static final String FIELD_TYPE_NAME = "typeName_s";

  public static final String PREFIXED_ID_PATTERN = "%s_([A-Za-z0-9]*)$";

  private static final String FILTER_FORMAT = "%s:%s";

  public static final String ID_FORMAT = "%s_%s";

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
        FIELD_TYPE_NAME, type.getSimpleName());
    solrQuery.addFilterQuery(typeNameFilterQuery);

    addQueryFields(solrQuery, query.getFields());

    String typeNameField = String.format("[child parentFilter=%s:%s]",
        FIELD_TYPE_NAME, type.getSimpleName());
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
    return type.getSimpleName().equals(value.toString());
  }

  protected <T> T getEntity(
      SolrDocument document, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    T entity = (T) ReflectionUtils.getInstance(type);
    List<Field> fields = ReflectionUtils.getFields(entity.getClass());
    String id = document.getFieldValue("id").toString();

    for (Field field : fields) {
      if (field.isAnnotationPresent(IndexField.class)) {
        IndexField annotation = field.getAnnotation(IndexField.class);
        String fieldName = StringUtils.isEmpty(annotation.name())
            ? field.getName() : annotation.name();

        switch (annotation.type()) {
          case OBJECT:
            if (document.getChildDocuments() == null) {
              ReflectionUtils.setValue(entity, fieldName, null);

            } else {
              Object childEntity = getChildEntity(document.getChildDocuments(),
                  field.getType(), id, fieldName);
              ReflectionUtils.setValue(entity, fieldName, childEntity);
            }
            break;
          case LIST:
            Class<?> listType = ReflectionUtils.getListType(field);

            if (listType.isEnum()) {
              Object fieldValue = document.getFieldValue(fieldName + "_s");

              if (fieldValue == null) {
                ReflectionUtils.setValue(entity,
                    fieldName, Collections.emptyList());

              } else {
                List<Enum> childEnums = new ArrayList<>();
                Class<Enum> listEnumType
                    = (Class<Enum>) ReflectionUtils.getListType(field);
                String[] childValues = fieldValue.toString().split(",");
                Arrays.asList(childValues).forEach(
                    enumValue -> childEnums.add(
                        Enum.valueOf(listEnumType, enumValue)));
                ReflectionUtils.setValue(entity, fieldName, childEnums);
              }
            } else if (document.getChildDocuments() == null) {
              ReflectionUtils.setValue(entity,
                  fieldName, Collections.emptyList());

            } else {
              List<?> childEntities = getChildEntities(
                  document.getChildDocuments(), listType, id, fieldName);
              ReflectionUtils.setValue(entity, fieldName, childEntities);
            }
            break;
          default:
            populateProperty(entity, document, field);
            break;
        }
      }
    }

    return entity;
  }

  protected <T> List<T> getChildEntities(
      List<SolrDocument> documents, Class<T> type,
      String prefix, String fieldName)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    List<T> entities = new ArrayList<>();

    for (SolrDocument document : documents) {
      String id = document.getFieldValue("id").toString();
      String regex = String.format(PREFIXED_ID_PATTERN, prefix);
      Pattern pattern = Pattern.compile(regex);

      if (pattern.matcher(id).find()
          && fieldName.equals(document.getFieldValue("fieldName_s"))) {

        T entity = getChildEntity(documents, type, id, fieldName);
        entities.add(entity);
      }
    }

    return entities;
  }

  protected <T> T getChildEntity(
      List<SolrDocument> documents, Class<T> type,
      String prefix, String fieldName)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    T entity = (T) ReflectionUtils.getInstance(type);
    List<Field> fields = ReflectionUtils.getFields(entity.getClass());

    Optional<SolrDocument> documentOptional = documents.stream().filter(
        isChildDocument(prefix, fieldName)).findFirst();

    if (!documentOptional.isPresent()) {
      return entity;
    }

    SolrDocument document = documentOptional.get();
    String id = document.getFieldValue("id").toString();

    for (Field field : fields) {
      if (field.isAnnotationPresent(IndexField.class)) {
        IndexField annotation = field.getAnnotation(IndexField.class);
        String childFieldName = StringUtils.isEmpty(annotation.name())
            ? field.getName() : annotation.name();

        switch (annotation.type()) {
          case OBJECT:
            if (document.getChildDocuments() == null) {
              ReflectionUtils.setValue(entity, childFieldName, null);

            } else {
              Object childEntity = getChildEntity(
                  documents, field.getType(), id, childFieldName);
              ReflectionUtils.setValue(entity, childFieldName, childEntity);
            }
            break;
          case LIST:
            Class<?> listType = ReflectionUtils.getListType(field);

            if (listType.isEnum()) {
              Object fieldValue = document.getFieldValue(childFieldName + "_s");

              if (fieldValue == null) {
                ReflectionUtils.setValue(entity,
                    childFieldName, Collections.emptyList());

              } else {
                List<Enum> childEnums = new ArrayList<>();
                Class<Enum> listEnumType
                    = (Class<Enum>) ReflectionUtils.getListType(field);
                String[] childValues = fieldValue.toString().split(",");
                Arrays.asList(childValues).forEach(
                    enumValue -> childEnums.add(
                        Enum.valueOf(listEnumType, enumValue)));
                ReflectionUtils.setValue(entity, childFieldName, childEnums);
              }
            } else {
              List<?> childEntities = getChildEntities(
                  documents, listType, id, childFieldName);
              ReflectionUtils.setValue(entity, childFieldName, childEntities);
            }
            break;
          default:
            populateProperty(entity, document, field);
            break;
        }
      }
    }

    return entity;
  }

  private Predicate<SolrDocument> isChildDocument(
      String prefix, String fieldName) {

    return document -> fieldName.equals(document.getFieldValue("fieldName_s"))
        && document.getFieldValue("id").toString().startsWith(prefix);
  }

  protected <T> void populateProperty(
      T entity, SolrDocument document, Field field)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    IndexField annotation = field.getAnnotation(IndexField.class);
    String fieldName = StringUtils.isEmpty(annotation.name())
        ? field.getName() : annotation.name();

    switch (annotation.type()) {
      case IDENTITY:
        String id = document.getFieldValue("id").toString();

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
      default:
        ReflectionUtils.setValue(entity, field.getName(),
            document.getFieldValue(fieldName + "_s"));
        break;
    }
  }

  public <T> void index(T entity)
      throws IOException {

    try {
      SolrInputDocument document = getDocument(entity);
      updateDocumentType(document, entity);
      updateDocumentId(document, "");

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
    document.addField(FIELD_TYPE_NAME, entity.getClass().getSimpleName());
  }

  protected void updateDocumentId(SolrInputDocument document, String prefix) {
    Object documentId = document.getFieldValue(FIELD_ID);

    if (StringUtils.isEmpty(prefix)) {
      document.getChildDocuments()
          .forEach(childDocument -> updateDocumentId(
              childDocument, documentId.toString()));

    } else {
      String childDocumentId = String.format(ID_FORMAT,
          prefix, documentId.toString());
      document.setField(FIELD_ID, childDocumentId);

      if (!ListUtils.isEmpty(document.getChildDocuments())) {
        document.getChildDocuments()
            .forEach(childDocument -> updateDocumentId(
                childDocument, childDocumentId));
      }
    }
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
