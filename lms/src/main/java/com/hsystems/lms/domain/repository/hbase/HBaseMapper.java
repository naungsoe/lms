package com.hsystems.lms.domain.repository.hbase;

import com.google.inject.Provider;

import com.hsystems.lms.MappingUtils;
import com.hsystems.lms.domain.repository.mapping.ColumnMap;
import com.hsystems.lms.domain.repository.mapping.DataMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by administrator on 8/8/16.
 */
public abstract class HBaseMapper {

  protected <T> void loadFields(T instance, Result result, DataMap dataMap)
      throws NoSuchFieldException, IllegalAccessException,
      InstantiationException, InvocationTargetException {

    for (ColumnMap columnMap : dataMap.getColumnMaps()) {
      if (columnMap.getField().getType() == List.class) {
        loadListField(instance, columnMap, result);
      } else {
        loadItemField(instance, columnMap, result);
      }
    }
  }

  protected <T> void loadListField(
      T instance, ColumnMap columnMap, Result result)
      throws NoSuchFieldException, IllegalAccessException,
      InstantiationException, InvocationTargetException {

    String columnFamily = columnMap.getColumnFamilyName();
    NavigableMap<byte[], byte[]> familyMap
        = result.getFamilyMap(Bytes.toBytes(columnFamily));
    Collection<byte[]> familyValues = familyMap.values();

    List<String> fieldValue = new ArrayList();
    for (byte[] familyValue : familyValues) {
      fieldValue.add(Bytes.toString(familyValue));
    }
    columnMap.setField(instance, fieldValue);
  }

  protected <T> void loadItemField(
      T instance, ColumnMap columnMap, Result result)
      throws NoSuchFieldException, IllegalAccessException,
      InstantiationException, InvocationTargetException {

    String columnFamily = columnMap.getColumnFamilyName();
    String identifier = columnMap.getColumnName();
    Object fieldValue;

    if (columnMap.getField().getType() == LocalDate.class) {
      fieldValue = HBaseUtils.getLocalDate(
          result, columnFamily, identifier);
    } else {
      fieldValue = HBaseUtils.getString(
          result, columnFamily, identifier);
    }
    columnMap.setField(instance, fieldValue);
  }
}
