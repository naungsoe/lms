package com.hsystems.lms.repository.solr.mapper;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.SchoolScoped;

import org.apache.solr.common.SolrInputDocument;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naungsoe on 4/11/16.
 */
public class DocumentMapper {

  public DocumentMapper() {

  }

  public <T> SolrInputDocument map(T entity)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    SolrInputDocument document = getDocument(entity);
    updateTypeName(document, entity);
    updateSchoolId(document, entity);
    updateFieldName(document, "");
    updateValueTypeId(document, "");
    updateParentId(document, "");
    updateChildrenId(document, "");
    return document;
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

    Object fieldValue = ReflectionUtils.getValue(entity, field, Object.class);

    if (fieldValue == null) {
      return;
    }

    IndexField annotation = field.getAnnotation(IndexField.class);
    String fieldName = StringUtils.isEmpty(annotation.name())
        ? field.getName() : annotation.name();
    Class<?> fieldType = field.getType();

    if (fieldType.isPrimitive() || (fieldType == String.class)) {
      document.addField(fieldName, fieldValue);

    } else if (fieldType.isEnum()) {
      document.addField(fieldName, fieldValue.toString());

    } else if (fieldType == LocalDateTime.class) {
      document.addField(fieldName,
          getDateTimeValue((LocalDateTime) fieldValue));

    } else if (fieldType == List.class) {
      if (ReflectionUtils.getListType(field).isEnum()) {
        List<Enum> enums = (List<Enum>) fieldValue;
        document.addField(fieldName, getEnumsValue(enums));

      } else if (ReflectionUtils.getListType(field) == String.class) {
        List<String> values = (List<String>) fieldValue;
        document.addField(fieldName, values);

      } else {
        List<SolrInputDocument> childDocuments
            = getChildDocuments(fieldName, (List) fieldValue);
        document.addChildDocuments(childDocuments);
      }
    } else {
      SolrInputDocument childDocument
          = getChildDocument(fieldName, fieldValue);
      document.addChildDocument(childDocument);
    }
  }

  protected String getDateTimeValue(LocalDateTime dateTime) {
    return String.format("%sZ", DateTimeUtils.toString(dateTime));
  }

  protected List<String> getEnumsValue(List<Enum> enums) {
    List<String> values = new ArrayList<>();
    enums.forEach(value -> values.add(value.toString()));
    return values;
  }

  protected <T> List<SolrInputDocument> getChildDocuments(
      String fieldName, List<T> entities)
      throws IllegalAccessException, NoSuchFieldException {

    List<SolrInputDocument> documents = new ArrayList<>();

    for (T entity : entities) {
      SolrInputDocument childDocument
          = getChildDocument(fieldName, entity);
      documents.add(childDocument);
    }

    return documents;
  }

  protected <T> SolrInputDocument getChildDocument(String fieldName, T entity)
      throws IllegalAccessException, NoSuchFieldException {

    SolrInputDocument childDocument = getDocument(entity);
    childDocument.setField(Constants.MEMBER_FIELD_NAME, fieldName);
    childDocument.setField(Constants.MEMBER_FIELD_TYPE_NAME,
        entity.getClass().getSimpleName());
    return childDocument;
  }

  protected <T> void updateTypeName(SolrInputDocument document, T entity) {
    document.addField(Constants.FIELD_TYPE_NAME,
        entity.getClass().getSimpleName());
  }

  protected <T> void updateSchoolId(SolrInputDocument document, T entity) {
    if (entity instanceof SchoolScoped) {
      SchoolScoped schoolScoped = (SchoolScoped) entity;
      document.addField(Constants.FIELD_SCHOOL_ID,
          schoolScoped.getSchool().getId());
    }
  }

  protected void updateFieldName(
      SolrInputDocument document, String parentFieldName) {

    Object fieldValue = document.getFieldValue(Constants.MEMBER_FIELD_NAME);
    String fieldName = (fieldValue == null) ? "" : fieldValue.toString();

    if (StringUtils.isNotEmpty(parentFieldName)) {
      fieldName = String.format(Constants.FORMAT_FIELD_NAME,
          parentFieldName, fieldName);
      document.setField(Constants.MEMBER_FIELD_NAME, fieldName);
    }

    List<SolrInputDocument> childDocuments = document.getChildDocuments();

    if (CollectionUtils.isNotEmpty(childDocuments)) {
      for (SolrInputDocument childDocument : childDocuments) {
        updateFieldName(childDocument, fieldName);
      }
    }
  }

  protected void updateValueTypeId(
      SolrInputDocument document, String parentId) {

    if (StringUtils.isNotEmpty(parentId)) {
      Object fieldValue = document.getFieldValue(Constants.FIELD_ID);

      if (fieldValue == null) {
        String id = CommonUtils.genUniqueKey();
        document.setField(Constants.FIELD_ID, id);
      }
    }

    List<SolrInputDocument> childDocuments = document.getChildDocuments();

    if (CollectionUtils.isNotEmpty(childDocuments)) {
      String id = StringUtils.isNotEmpty(parentId)
          ? parentId : document.getFieldValue(Constants.FIELD_ID).toString();

      for (SolrInputDocument childDocument : childDocuments) {
        updateValueTypeId(childDocument, id);
      }
    }
  }

  protected void updateParentId(SolrInputDocument document, String parentId) {
    if (StringUtils.isNotEmpty(parentId)) {
      document.setField(Constants.FIELD_PARENT_ID, parentId);
    }

    List<SolrInputDocument> childDocuments = document.getChildDocuments();

    if (CollectionUtils.isNotEmpty(childDocuments)) {
      String id = document.getFieldValue(Constants.FIELD_ID).toString();

      for (SolrInputDocument childDocument : childDocuments) {
        updateParentId(childDocument, id);
      }
    }
  }

  protected void updateChildrenId(SolrInputDocument document, String parentId) {
    if (StringUtils.isNotEmpty(parentId)) {
      String id = document.getFieldValue(Constants.FIELD_ID).toString();
      document.setField(Constants.FIELD_ID,
          String.format(Constants.FORMAT_FIELD_ID, parentId, id));
    }

    List<SolrInputDocument> childDocuments = document.getChildDocuments();
    String rootId = StringUtils.isNotEmpty(parentId)
        ? parentId : document.getFieldValue(Constants.FIELD_ID).toString();

    if (CollectionUtils.isNotEmpty(childDocuments)) {
      for (SolrInputDocument childDocument : childDocuments) {
        updateChildrenId(childDocument, rootId);
      }
    }
  }
}
