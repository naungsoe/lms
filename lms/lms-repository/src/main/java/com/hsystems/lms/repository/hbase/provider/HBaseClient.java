package com.hsystems.lms.repository.hbase.provider;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by administrator on 31/10/16.
 */
public class HBaseClient {

  public Result get(Get get, String tableName)
      throws IOException {

    Configuration configuration = HBaseConfiguration.create();

    try (Connection connection
             = ConnectionFactory.createConnection(configuration)) {

      Table table = connection.getTable(TableName.valueOf(tableName));
      Result result = table.get(get);
      table.close();
      return result;
    }
  }

  public List<Result> scan(Scan scan, String tableName)
      throws IOException {

    Configuration configuration = HBaseConfiguration.create();

    try (Connection connection
             = ConnectionFactory.createConnection(configuration)) {

      Table table = connection.getTable(TableName.valueOf(tableName));
      ResultScanner scanner = table.getScanner(scan);
      Iterator<Result> iterator = scanner.iterator();
      List<Result> results = new ArrayList<>();

      if (!iterator.hasNext()) {
        return results;
      }

      while (iterator.hasNext()) {
        results.add(iterator.next());
      }

      scanner.close();
      table.close();
      return results;
    }
  }

  public void put(Put put, String tableName)
      throws IOException {
    
    Configuration configuration = HBaseConfiguration.create();

    try (Connection connection
             = ConnectionFactory.createConnection(configuration)) {

      Table table = connection.getTable(TableName.valueOf(tableName));
      table.put(put);
      table.close();
    }
  }
}
