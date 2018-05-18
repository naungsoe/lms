package com.hsystems.lms.hbase;

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
public final class HBaseClient {

  private Configuration configuration;

  private volatile Connection connection;

  public HBaseClient() {
    configuration = HBaseConfiguration.create();
  }

  public List<Result> scan(Scan scan, String tableName)
      throws IOException {

    Table table = getTable(tableName);
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

  private Table getTable(String tableName)
      throws IOException {

    Connection connection = getConnection();
    return connection.getTable(TableName.valueOf(tableName));
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

  public List<Result> get(List<Get> gets, String tableName)
      throws IOException {

    Table table = getTable(tableName);
    Result[] results = table.get(gets);
    table.close();
    return Arrays.asList(results);
  }

  public Result get(Get get, String tableName)
      throws IOException {

    Table table = getTable(tableName);
    Result result = table.get(get);
    table.close();
    return result;
  }

  public void put(Put put, String tableNam)
      throws IOException {

    put(Arrays.asList(put), tableNam);
  }

  public void put(List<Put> puts, String tableName)
      throws IOException {

    Table table = getTable(tableName);
    table.put(puts);
    table.close();
  }

  public void delete(Delete delete, String tableName)
      throws IOException {

    delete(Arrays.asList(delete), tableName);
  }

  public void delete(List<Delete> deletes, String tableName)
      throws IOException {

    Table table = getTable(tableName);
    table.delete(deletes);
    table.close();
  }
}