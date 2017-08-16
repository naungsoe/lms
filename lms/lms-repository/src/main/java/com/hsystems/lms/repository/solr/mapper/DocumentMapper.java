package com.hsystems.lms.repository.solr.mapper;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import org.apache.solr.common.SolrInputDocument;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naungsoe on 4/11/16.
 */
public class DocumentMapper extends Mapper<SolrInputDocument> {

  protected static final String FIELD_PARENT_ID = "parentId";

  protected static final String FIELD_NAME_FORMAT = "%s.%s";

  @Override
  public <T> SolrInputDocument map(T entity)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    SolrInputDocument document = getDocument(entity);
    updateParentId(document, "");
    updateFieldName(document, "");
    document.addField(FIELD_TYPE_NAME,
        entity.getClass().getSimpleName());
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
    childDocument.setField(MEMBER_FIELD_NAME, fieldName);
    childDocument.setField(MEMBER_FIELD_TYPE_NAME,
        entity.getClass().getSimpleName());
    return childDocument;
  }

  protected void updateParentId(SolrInputDocument document, String parentId) {
    String id = document.getFieldValue(FIELD_ID).toString();

    if (StringUtils.isNotEmpty(parentId)) {
      document.setField(FIELD_PARENT_ID, parentId);
    }

    if (CollectionUtils.isNotEmpty(document.getChildDocuments())) {
      document.getChildDocuments().forEach(
          childDocument -> updateParentId(childDocument, id));
    }
  }

  protected void updateFieldName(
      SolrInputDocument document, String parentFieldName) {

    String fieldName = (document.getFieldValue(MEMBER_FIELD_NAME) == null)
        ? "" : document.getFieldValue(MEMBER_FIELD_NAME).toString();

    if (StringUtils.isNotEmpty(parentFieldName)) {
      document.setField(MEMBER_FIELD_NAME, String.format(
          FIELD_NAME_FORMAT, parentFieldName, fieldName));
    }

    List<SolrInputDocument> childDocuments = document.getChildDocuments();

    if (CollectionUtils.isNotEmpty(childDocuments)) {
      childDocuments.forEach(childDocument -> updateFieldName(
          childDocument, fieldName));
    }
  }
}
