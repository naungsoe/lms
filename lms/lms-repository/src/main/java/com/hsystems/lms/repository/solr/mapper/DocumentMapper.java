package com.hsystems.lms.repository.solr.mapper;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ListUtils;
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

  private static final String ID_FORMAT = "%s_%s";

  @Override
  public <T> SolrInputDocument map(T entity)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    SolrInputDocument document = getDocument(entity);
    updateDocumentId(document, "");
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
      SolrInputDocument document = getChildDocument(fieldName, entity);
      documents.add(document);
    }

    return documents;
  }

  protected <T> SolrInputDocument getChildDocument(String fieldName, T entity)
      throws IllegalAccessException, NoSuchFieldException {

    SolrInputDocument document = getDocument(entity);
    document.setField(FIELD_NAME, fieldName);
    return document;
  }

  protected void updateDocumentId(SolrInputDocument document, String prefix) {
    Object documentId = document.getFieldValue(FIELD_ID);

    if (StringUtils.isEmpty(prefix)) {
      document.getChildDocuments().forEach(childDocument -> {
        updateDocumentId(childDocument, documentId.toString());
      });
    } else {
      String childDocumentId = String.format(ID_FORMAT,
          prefix, documentId.toString());
      document.setField(FIELD_ID, childDocumentId);

      if (!ListUtils.isEmpty(document.getChildDocuments())) {
        document.getChildDocuments().forEach(childDocument -> {
          updateDocumentId(childDocument, childDocumentId);
        });
      }
    }
  }
}
