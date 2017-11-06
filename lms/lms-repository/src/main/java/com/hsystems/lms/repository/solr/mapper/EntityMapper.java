package com.hsystems.lms.repository.solr.mapper;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.Constants;

import org.apache.solr.common.SolrDocument;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by naungse on 2/12/16.
 */
public class EntityMapper {

  private final Map<String, Class<?>> typeMap;

  public EntityMapper(Map<String, Class<?>> typeMap) {
    this.typeMap = typeMap;
  }

  public <T> T map(SolrDocument document, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException,
      ClassNotFoundException {

    T entity = ReflectionUtils.isInstantiable(type)
        ? (T) ReflectionUtils.getInstance(type)
        : (T) ReflectionUtils.getInstance(getSubType(document, type));
    List<Field> fields = ReflectionUtils.getFields(entity.getClass());
    String id = document.getFieldValue(Constants.FIELD_ID).toString();

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
          List<SolrDocument> documents = document.getChildDocuments();
          List<?> childEntities = getChildEntities(documents, id, fieldName);
          ReflectionUtils.setValue(entity, fieldName, childEntities);
        }
      } else {
        List<SolrDocument> documents = document.getChildDocuments();
        Optional<?> entityOptional = getChildEntity(documents, id, fieldName);

        if (entityOptional.isPresent()) {
          ReflectionUtils.setValue(entity,
              fieldName, entityOptional.get());
        }
      }
    }

    return entity;
  }

  protected <T> Class<?> getSubType(
      SolrDocument document, Class<T> type) {

    String packageName = type.getPackage().getName();
    String typeName = document.getFieldValue(
        Constants.FIELD_TYPE_NAME).toString();
    typeName = String.format("%s.%s", packageName, typeName);

    try {
      return Class.forName(typeName);

    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
          "error retrieving sub-type", e);
    }
  }

  protected String getFieldName(Field field) {
    IndexField annotation = field.getAnnotation(IndexField.class);
    return StringUtils.isEmpty(annotation.name())
        ? field.getName() : annotation.name();
  }

  protected <T> List<T> getChildEntities(
      List<SolrDocument> documents, String parentId, String fieldName)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    if (CollectionUtils.isEmpty(documents)) {
      return Collections.emptyList();
    }

    List<T> entities = new ArrayList<>();

    for (SolrDocument document : documents) {
      if (isChildDocument(document, parentId, fieldName)) {
        Optional<T> entityOptional
            = getChildEntity(documents, parentId, fieldName);

        if (entityOptional.isPresent()) {
          entities.add(entityOptional.get());
        }
      }
    }

    return entities;
  }

  protected boolean isChildDocument(
      SolrDocument document, String parentId, String fieldName) {

    Object fullFieldName = document.getFieldValue(Constants.MEMBER_FIELD_NAME);
    return document.getFieldValue("parentId").equals(parentId)
        && fullFieldName.toString().endsWith(fieldName)
        && !"true".equals(document.getFieldValue("processed"));
  }

  protected <T> Optional<T> getChildEntity(
      List<SolrDocument> documents, String parentId, String fieldName)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    Optional<SolrDocument> documentOptional = documents.stream()
        .filter(document -> isChildDocument(document, parentId, fieldName))
        .findFirst();

    if (!documentOptional.isPresent()) {
      return Optional.empty();
    }

    SolrDocument document = documentOptional.get();
    String id = document.getFieldValue(Constants.FIELD_ENTITY_ID).toString();
    String typeName = document.getFieldValue(
        Constants.MEMBER_FIELD_TYPE_NAME).toString();
    T entity = (T) ReflectionUtils.getInstance(typeMap.get(typeName));
    List<Field> fields = ReflectionUtils.getFields(entity.getClass());

    for (Field field : fields) {
      if (!field.isAnnotationPresent(IndexField.class)) {
        continue;
      }

      String memberFieldName = getFieldName(field);
      Class<?> memberFieldType = field.getType();

      if (memberFieldType.isPrimitive() || memberFieldType.isEnum()
          || (memberFieldType == LocalDateTime.class)
          || (memberFieldType == String.class)) {

        populateProperty(entity, document, field);

      } else if (memberFieldType == List.class) {
        Class<?> listType = ReflectionUtils.getListType(field);
        Object fieldValue;

        if (listType.isEnum()) {
          fieldValue = document.getFieldValue(memberFieldName);

          if (fieldValue == null) {
            ReflectionUtils.setValue(entity,
                memberFieldName, Collections.emptyList());

          } else {
            List<String> enumValues = (List<String>) fieldValue;
            Class<Enum> enumType
                = (Class<Enum>) ReflectionUtils.getListType(field);

            List<Enum> enums = new ArrayList<>();
            enumValues.forEach(enumValue -> enums.add(
                Enum.valueOf(enumType, enumValue)));
            ReflectionUtils.setValue(entity, memberFieldName, enums);
          }
        } else if (listType == String.class) {
          fieldValue = document.getFieldValue(memberFieldName);

          if (fieldValue == null) {
            ReflectionUtils.setValue(entity,
                memberFieldName, Collections.emptyList());

          } else {
            List<String> values = (List<String>) fieldValue;
            ReflectionUtils.setValue(entity, memberFieldName, values);
          }
        } else {
          List<?> childEntities
              = getChildEntities(documents, id, memberFieldName);
          ReflectionUtils.setValue(entity, memberFieldName, childEntities);
        }
      } else {
        Optional<?> entityOptional
            = getChildEntity(documents, id, memberFieldName);

        if (entityOptional.isPresent()) {
          ReflectionUtils.setValue(entity,
              memberFieldName, entityOptional.get());
        }
      }
    }

    document.setField("processed", "true");
    return Optional.of(entity);
  }

  protected <T> void populateProperty(
      T entity, SolrDocument document, Field field)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    String fieldName = getFieldName(field);
    Class<?> fieldType = field.getType();

    if (fieldName.equals(Constants.FIELD_ID)) {
      String id = (document.getFieldValue(Constants.FIELD_ENTITY_ID) != null)
          ? document.getFieldValue(Constants.FIELD_ENTITY_ID).toString()
          : document.getFieldValue(Constants.FIELD_ID).toString();
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