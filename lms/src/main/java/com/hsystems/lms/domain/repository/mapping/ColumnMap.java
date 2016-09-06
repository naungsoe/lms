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

  private String columnFamilyName;

  private String columnName;

  private String fieldName;

  private Field field;

  private DataMap dataMap;

  public ColumnMap(
      String columnFamilyName, String columnName,
      String fieldName, DataMap dataMap)
      throws ApplicationException {

    this.columnFamilyName = columnFamilyName;
    this.columnName = columnName;
    this.fieldName = fieldName;
    this.dataMap = dataMap;
    initField();
  }

  protected void initField() throws ApplicationException {
    try {
      if (fieldName.indexOf('.') > 0) {
        String[] fieldPaths = fieldName.split(Pattern.quote("."));
        Class fieldClass = this.dataMap.getDomainClass()
            .getDeclaredField(fieldPaths[0]).getType();

        for (int i = 1; i < fieldPaths.length; i++) {
          if ((i + 1) < fieldPaths.length) {
            fieldClass = fieldClass.getDeclaredField(fieldPaths[i]).getType();
          } else {
            this.field = fieldClass.getDeclaredField(fieldPaths[i]);
            break;
          }
        }
      } else {
        this.field = this.dataMap.getDomainClass()
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
    } catch (Exception e) {
      throw new ApplicationException(
          "error in setting " + fieldName, e);
    }
  }
}
