package com.hsystems.lms.domain.repository.mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 13/8/16.
 */
public final class DataMap {

  private final Class domainClass;

  private final String tableName;

  private final List<ColumnMap> columnMaps;

  public DataMap(Class domainClass, String tableName) {
    this.domainClass = domainClass;
    this.tableName = tableName;
    this.columnMaps = new ArrayList<>();
  }

  public Class getDomainClass() {
    return domainClass;
  }

  public String getTableName() {
    return tableName;
  }

  public List<ColumnMap> getColumnMaps() { return columnMaps; }

  public void addColumn(
      String columnFamilyName, String columnName,
      String fieldName)
      throws NoSuchFieldException {

    columnMaps.add(new ColumnMap(
        columnFamilyName,
        columnName,
        fieldName,
        this
    ));
  }
}