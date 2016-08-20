package com.hsystems.lms.domain.repository.mapping;

import com.hsystems.lms.exception.ApplicationException;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * Created by administrator on 13/8/16.
 */
public class ColumnMap {

  private final String columnFamilyName;

  private final String columnName;

  private final String fieldName;

  private final Field field;

  private final DataMap dataMap;

  public ColumnMap(
      String columnFamilyName, String columnName,
      String fieldName, DataMap dataMap)
      throws ApplicationException {

    if (StringUtils.countMatches(fieldName, ".") > 2) {
      throw new ApplicationException(
          "unable to set up nested field: " + fieldName);
    }

    this.columnFamilyName = columnFamilyName;
    this.columnName = columnName;
    this.fieldName = fieldName;
    this.dataMap = dataMap;

    try {
      if (fieldName.indexOf('.') > 0) {
        String[] fieldPaths = fieldName.split(Pattern.quote("."));
        Class fieldClass = this.dataMap.getDomainClass()
            .getDeclaredField(fieldPaths[0]).getType();
        this.field = fieldClass.getDeclaredField(fieldPaths[1]);
      } else {
        this.field = dataMap.getDomainClass()
            .getDeclaredField(fieldName);
      }
    } catch (Exception e) {
      throw new ApplicationException(
          "unable to set up field: " + fieldName, e);
    }
  }

  public String getColumnFamilyName() {
    return columnFamilyName;
  }

  public String getColumnName() {
    return columnName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setField(Object object, Object columnValue)
      throws ApplicationException {

    try {
      field.setAccessible(true);
      field.set(object, columnValue);
    } catch (Exception e) {
      throw new ApplicationException(
          "error in setting " + fieldName, e);
    }
  }

  public void setFieldPath(Object object, Object columnValue)
      throws ApplicationException {

    try {
      String[] fieldPaths = fieldName.split(Pattern.quote("."));
      Class fieldClass = this.dataMap.getDomainClass()
          .getDeclaredField(fieldPaths[0]).getType();
      Field classField = dataMap.getDomainClass()
          .getDeclaredField(fieldPaths[0]);
      classField.setAccessible(true);

      Object classInstance = classField.get(object);

      if (classInstance == null) {
        classInstance = MappingUtils.getInstance(classField.getType());
        MappingUtils.setField(object, fieldPaths[0], classInstance);
      }
      field.setAccessible(true);
      field.set(classInstance, columnValue);
    } catch (Exception e) {
      throw new ApplicationException(
          "error in setting " + fieldName, e);
    }
  }
}
