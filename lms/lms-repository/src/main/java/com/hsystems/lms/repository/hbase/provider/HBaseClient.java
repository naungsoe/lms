package com.hsystems.lms.repository.hbase.provider;

import com.hsystems.lms.common.annotation.IndexCollection;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by administrator on 31/10/16.
 */
public class HBaseClient {

  private Configuration configuration;

  private volatile Connection connection;

  public HBaseClient() {
    configuration = HBaseConfiguration.create();
  }

  public <T> List<Result> get(List<Get> gets, Class<T> type)
      throws IOException {

    Table table = getTable(getTableName(type));
    Result[] results = table.get(gets);
    table.close();
    return Arrays.asList(results);
  }

  public <T> Result get(Get get, Class<T> type)
      throws IOException {

    Table table = getTable(getTableName(type));
    Result result = table.get(get);
    table.close();
    return result;
  }

  private Connection getConnection()
      throws IOException {

    Connection instance = connection;

    if (instance == null) {
      synchronized (this) {
        instance = connection;

        if (instance == null) {
          connection = ConnectionFactory.createConnection(configuration);
          instance = connection;
        }
      }
    }

    return instance;
  }

  private Table getTable(TableName tableName)
      throws IOException {

    return getConnection().getTable(tableName);
  }

  private <T> TableName getTableName(Class<T> type) {
    IndexCollection annotation = type.getAnnotation(IndexCollection.class);
    String namespace = annotation.namespace();
    String collection = StringUtils.isEmpty(annotation.name())
        ? type.getSimpleName() : annotation.name();
    String tableName = StringUtils.isEmpty(namespace)
        ? collection : String.format("%s:%s", namespace, collection);
    return TableName.valueOf(tableName);
  }

  public <T> List<Result> scan(Scan scan, Class<T> type)
      throws IOException {

    Table table = getTable(getTableName(type));
    ResultScanner scanner = table.getScanner(scan);
    Iterator<Result> iterator = scanner.iterator();

    if (!iterator.hasNext()) {
      return Collections.emptyList();
    }

    List<Result> results = new ArrayList<>();

    while (iterator.hasNext()) {
      results.add(iterator.next());
    }

    scanner.close();
    table.close();
    return results;
  }

  public <T> void put(Put put, Class<T> type)
      throws IOException {

    put(Arrays.asList(put), type);
  }

  public <T> void put(List<Put> puts, Class<T> type)
      throws IOException {

    Table table = getTable(getTableName(type));
    table.put(puts);
    table.close();
  }

  public <T> void delete(Delete delete, Class<T> type)
      throws IOException {

    delete(Arrays.asList(delete), type);
  }

  public <T> void delete(List<Delete> deletes, Class<T> type)
      throws IOException {

    Table table = getTable(getTableName(type));
    table.delete(deletes);
    table.close();
  }
}
