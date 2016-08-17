package com.hsystems.lms.domain.repository.mapping;

import com.hsystems.lms.exception.ApplicationException;

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

    this.columnFamilyName = columnFamilyName;
    this.columnName = columnName;
    this.fieldName = fieldName;
    this.dataMap = dataMap;

    try {
      if (fieldName.indexOf('.') > 0) {
        String[] fieldPaths = fieldName.split(Pattern.quote("."));
        Class[] fieldClasses = getFieldOwningClasses(fieldPaths, fieldName);
        String fieldPath =fieldPaths[fieldPaths.length - 1];
        Class fieldClass = fieldClasses[fieldClasses.length - 1];
        this.field = fieldClass.getDeclaredField(fieldPath);
      } else {
        this.field = dataMap.getDomainClass()
            .getDeclaredField(fieldName);
      }
      this.field.setAccessible(true);
    } catch (Exception e) {
      throw new ApplicationException(
          "unable to set up field: " + fieldName, e);
    }
  }

  private Class[] getFieldOwningClasses(
      String[] fieldPaths, String fieldName)
      throws NoSuchFieldException {

    Class[] fieldClasses = new Class[(fieldPaths.length - 1)];

    for (int i = 0; (i + 1) < fieldPaths.length; i++) {
      if (i == 0) {
        fieldClasses[i] = dataMap.getDomainClass()
            .getDeclaredField(fieldPaths[i]).getType();
      } else {
        fieldClasses[i] = fieldClasses[i - 1]
            .getDeclaredField(fieldPaths[i]).getType();
      }
    }
    return fieldClasses;
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
      Class[] fieldClasses = getFieldOwningClasses(fieldPaths, fieldName);
      Object fieldInstance = null;

      for (int i = 0; i < fieldClasses.length; i++) {
        Object instance = MappingUtils.getInstance(fieldClasses[i]);

        if (i == 0) {
          MappingUtils.setField(object, fieldPaths[i], instance);
        } else {
          MappingUtils.setField(fieldInstance, fieldPaths[i], instance);
        }
        fieldInstance = instance;
      }
      field.set(fieldInstance, columnValue);
    } catch (Exception e) {
      throw new ApplicationException(
          "error in setting " + fieldName, e);
    }
  }
}
