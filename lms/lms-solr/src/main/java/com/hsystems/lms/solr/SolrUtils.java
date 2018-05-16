package com.hsystems.lms.solr;

import com.hsystems.lms.common.util.DateTimeUtils;

import org.apache.solr.common.SolrDocument;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public final class SolrUtils {

  private static final String FIELD_NAME_FIELD = "fieldName";
  private static final String PARENT_ID_FIELD = "parentId";

  private static final String VALUE_SEPARATOR = ",";

  public static Predicate<SolrDocument> isChildDocument(
      String parentId, String fieldName) {

    return document  -> {
      Object parentIdValue = document.getFieldValue(PARENT_ID_FIELD);
      Object fieldNameValue = document.getFieldValue(FIELD_NAME_FIELD);
      return parentId.equals(parentIdValue.toString())
          && fieldName.equals(fieldNameValue.toString());
    };
  }

  public static boolean getBoolean(
      SolrDocument document, String fieldName) {

    String value = getString(document, fieldName);
    return Boolean.parseBoolean(value);
  }

  public static int getInteger(
      SolrDocument document, String fieldName) {

    String value = getString(document, fieldName);
    return Integer.parseInt(value);
  }

  public static long getLong(
      SolrDocument document, String fieldName) {

    String value = getString(document, fieldName);
    return Long.parseLong(value);
  }

  public static List<String> getStrings(
      SolrDocument document, String fieldName) {

    String value = getString(document, fieldName);
    String[] items = value.split(VALUE_SEPARATOR);
    return Arrays.asList(items);
  }

  public static String getString(
      SolrDocument document, String fieldName) {

    Object value = document.getFieldValue(fieldName);
    return value.toString();
  }

  public static LocalDateTime getDateTime(
      SolrDocument document, String fieldName) {

    Object value = document.getFieldValue(fieldName);
    return DateTimeUtils.toLocalDateTime((Date) value);
  }
}