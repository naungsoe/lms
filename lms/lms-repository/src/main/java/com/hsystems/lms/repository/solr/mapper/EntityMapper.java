package com.hsystems.lms.repository.solr.mapper;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Entity;

import org.apache.solr.common.SolrDocument;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungse on 2/12/16.
 */
public class EntityMapper extends Mapper<Entity> {

  private Class<?> type;

  public EntityMapper(Class<?> type) {
    this.type = type;
  }

  @Override
  public <T> Entity map(T source)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    SolrDocument document = (SolrDocument) source;
    return getEntity(document);
  }

  protected <T> T getEntity(SolrDocument document)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    T entity = (T) ReflectionUtils.getInstance(type);
    List<Field> fields = ReflectionUtils.getFields(entity.getClass());
    String id = document.getFieldValue(FIELD_ID).toString();

    for (Field field : fields) {
      if (!field.isAnnotationPresent(IndexField.class)) {
        continue;
      }

      String fieldName = getFieldName(field);
      Class<?> fieldType = field.getType();

      if (fieldType.isPrimitive() || fieldType.isEnum()
          || (fieldType == LocalDateTime.class)
          || (fieldType == String.class)) {

        populateProperty(entity, document, field);

      } else if (fieldType == List.class) {
        Class<?> listType = ReflectionUtils.getListType(field);
        Object fieldValue;

        if (listType.isEnum()) {
          fieldValue = document.getFieldValue(fieldName);

          if (fieldValue == null) {
            ReflectionUtils.setValue(entity,
                fieldName, Collections.emptyList());

          } else {
            List<String> enumValues = (List<String>) fieldValue;
            Class<Enum> enumType
                = (Class<Enum>) ReflectionUtils.getListType(field);

            List<Enum> enums = new ArrayList<>();
            enumValues.forEach(enumValue -> enums.add(
                Enum.valueOf(enumType, enumValue)));
            ReflectionUtils.setValue(entity, fieldName, enums);
          }
        } else if (listType == String.class) {
          fieldValue = document.getFieldValue(fieldName);

          if (fieldValue == null) {
            ReflectionUtils.setValue(entity,
                fieldName, Collections.emptyList());

          } else {
            List<String> values = (List<String>) fieldValue;
            ReflectionUtils.setValue(entity, fieldName, values);
          }
        } else {
          List<?> childEntities = getChildEntities(
              document.getChildDocuments(), listType, id, fieldName);
          ReflectionUtils.setValue(entity, fieldName, childEntities);
        }
      } else {
        Object childEntity = getChildEntity(
            document.getChildDocuments(), field.getType(), id, fieldName);
        ReflectionUtils.setValue(entity, fieldName, childEntity);
      }
    }

    return entity;
  }

  protected String getFieldName(Field field) {
    IndexField annotation = field.getAnnotation(IndexField.class);
    return StringUtils.isEmpty(annotation.name())
        ? field.getName() : annotation.name();
  }

  protected <T> List<T> getChildEntities(
      List<SolrDocument> documents, Class<T> type,
      String parentId, String fieldName)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    if (CollectionUtils.isEmpty(documents)) {
      return Collections.emptyList();
    }

    List<T> entities = new ArrayList<>();

    for (SolrDocument document : documents) {
      if (isChildDocument(document, parentId, fieldName)) {
        T entity = getChildEntity(documents, type, parentId, fieldName);
        entities.add(entity);
      }
    }

    return entities;
  }

  protected boolean isChildDocument(
      SolrDocument document, String parentId, String fieldName) {

    Object fullFieldName = document.getFieldValue(FIELD_NAME);
    return document.getFieldValue("parentId").equals(parentId)
        && fullFieldName.toString().endsWith(fieldName)
        && !"true".equals(document.getFieldValue("processed"));
  }

  protected <T> T getChildEntity(
      List<SolrDocument> documents, Class<T> type,
      String parentId, String fieldName)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    T entity = (T) ReflectionUtils.getInstance(type);
    List<Field> fields = ReflectionUtils.getFields(entity.getClass());

    Optional<SolrDocument> documentOptional = documents.stream()
        .filter(document -> isChildDocument(document, parentId, fieldName))
        .findFirst();

    if (!documentOptional.isPresent()) {
      return entity;
    }

    SolrDocument document = documentOptional.get();
    String id = document.getFieldValue(FIELD_ID).toString();

    for (Field field : fields) {
      if (!field.isAnnotationPresent(IndexField.class)) {
        continue;
      }

      String childFieldName = getFieldName(field);
      Class<?> childFieldType = field.getType();

      if (childFieldType.isPrimitive() || childFieldType.isEnum()
          || (childFieldType == LocalDateTime.class)
          || (childFieldType == String.class)) {

        populateProperty(entity, document, field);

      } else if (childFieldType == List.class) {
        Class<?> listType = ReflectionUtils.getListType(field);
        Object fieldValue;

        if (listType.isEnum()) {
          fieldValue = document.getFieldValue(childFieldName);

          if (fieldValue == null) {
            ReflectionUtils.setValue(entity,
                childFieldName, Collections.emptyList());

          } else {
            List<String> enumValues = (List<String>) fieldValue;
            Class<Enum> enumType
                = (Class<Enum>) ReflectionUtils.getListType(field);

            List<Enum> enums = new ArrayList<>();
            enumValues.forEach(enumValue -> enums.add(
                Enum.valueOf(enumType, enumValue)));
            ReflectionUtils.setValue(entity, childFieldName, enums);
          }
        } else if (listType == String.class) {
          fieldValue = document.getFieldValue(childFieldName);

          if (fieldValue == null) {
            ReflectionUtils.setValue(entity,
                childFieldName, Collections.emptyList());

          } else {
            List<String> values = (List<String>) fieldValue;
            ReflectionUtils.setValue(entity, childFieldName, values);
          }
        } else {
          List<?> childEntities = getChildEntities(
              documents, listType, id, childFieldName);
          ReflectionUtils.setValue(entity, childFieldName, childEntities);
        }
      } else {
        Object childEntity = getChildEntity(
            documents, field.getType(), id, childFieldName);
        ReflectionUtils.setValue(entity, childFieldName, childEntity);
      }
    }

    document.setField("processed", "true");
    return entity;
  }

  protected <T> void populateProperty(
      T entity, SolrDocument document, Field field)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    String fieldName = getFieldName(field);
    Class<?> fieldType = field.getType();

    if (fieldName.equals(FIELD_ID)) {
      Object id = document.getFieldValue(FIELD_ID);
      ReflectionUtils.setValue(entity, field.getName(), id);

    } else if (fieldType.isPrimitive()) {
      ReflectionUtils.setValue(entity, field.getName(),
          document.getFieldValue(fieldName));

    } else if (fieldType.isEnum()) {
      Object value = document.getFieldValue(fieldName);
      Class<Enum> enumType = (Class<Enum>) field.getType();
      ReflectionUtils.setValue(entity, field.getName(),
          Enum.valueOf(enumType, value.toString()));

    } else if (fieldType == LocalDateTime.class) {
      Object value = document.getFieldValue(fieldName);

      if (value != null) {
        ReflectionUtils.setValue(entity, field.getName(),
            DateTimeUtils.toLocalDateTime((Date) value));
      }
    } else if (fieldType == String.class) {
      ReflectionUtils.setValue(entity, field.getName(),
          document.getFieldValue(fieldName));
    }
  }
}