package com.hsystems.lms.domain.repository.mapping;

import com.hsystems.lms.MappingUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

/**
 * Created by administrator on 13/8/16.
 */
public final class ColumnMap {

  private final String columnFamilyName;

  private final String columnName;

  private final String fieldName;

  private final DataMap dataMap;

  private final Field field;

  ColumnMap(
      String columnFamilyName, String columnName,
      String fieldName, DataMap dataMap)
      throws NoSuchFieldException {

    this.columnFamilyName = columnFamilyName;
    this.columnName = columnName;
    this.fieldName = fieldName;
    this.dataMap = dataMap;
    this.field = getClassField();
  }

  protected Field getClassField()
      throws NoSuchFieldException {

    if (fieldName.indexOf('.') > 0) {
      String[] fieldPaths = fieldName.split(Pattern.quote("."));
      Class fieldClass = dataMap.getDomainClass()
          .getDeclaredField(fieldPaths[0]).getType();

      for (int i = 1; i < fieldPaths.length; i++) {
        if ((i + 1) < fieldPaths.length) {
          fieldClass = fieldClass.getDeclaredField(fieldPaths[i]).getType();
        } else {
          return fieldClass.getDeclaredField(fieldPaths[i]);
        }
      }
    } else {
      return dataMap.getDomainClass().getDeclaredField(fieldName);
    }

    throw new NoSuchElementException(
        "error setting up field");
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

  public Field getField(){ return field; }

  public <T,S> void setField(T object, S columnValue)
      throws NoSuchFieldException, IllegalAccessException,
      InstantiationException, InvocationTargetException {

    if (fieldName.indexOf('.') > 0) {
      String[] fieldPaths = fieldName.split(Pattern.quote("."));
      Field classField = dataMap.getDomainClass()
          .getDeclaredField(fieldPaths[0]);
      Object classInstance = classField.get(object);

      for (int i = 0; i < fieldPaths.length; i++) {
        if ((i + 1) < fieldPaths.length) {
          if (classInstance == null) {
            classInstance = MappingUtils.getInstance(classField.getType());
            MappingUtils.setField(object, fieldPaths[i], classInstance);
          }
          classField = classInstance.getClass()
              .getDeclaredField(fieldPaths[i + 1]);
          classInstance = classField.get(classInstance);
        } else {
          field.setAccessible(true);
          field.set(classInstance, columnValue);
        }
      }
    } else {
      field.setAccessible(true);
      field.set(object, columnValue);
    }
  }
}
