package com.hsystems.lms.provider.hbase;

import com.google.inject.Provider;

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
import java.util.Optional;
import java.util.Properties;

/**
 * Created by administrator on 23/9/16.
 */
public final class HBaseClient {

  private Provider<Properties> propertiesProvider;

  HBaseClient() {

  }

  HBaseClient(Provider<Properties> propertiesProvider) {
    this.propertiesProvider = propertiesProvider;
  }

  public ResultScanner scanResults(Scan scan, String tableName)
      throws IOException {

    Connection connection = getConnection();
    Table table = getTable(connection, tableName);
    ResultScanner results;

    try {
      results = table.getScanner(scan);
    } finally {
      table.close();
      connection.close();
    }
    return results;
  }

  public Optional<Result> getResult(Get get, String tableName)
      throws IOException {

    Connection connection = getConnection();
    Table table = getTable(connection, tableName);
    Result result;

    try {
      result = table.get(get);
    } finally {
      table.close();
      connection.close();
    }

    if (result == null) {
      return Optional.empty();
    }
    return Optional.of(result);
  }

  public void putRecord(Put put, String tableName)
      throws IOException {

    Connection connection = getConnection();
    Table table = getTable(connection, tableName);

    try {
      table.put(put);
    } finally {
      table.close();
      connection.close();
    }
  }

  protected Connection getConnection()
      throws IOException {

    Configuration config = HBaseConfiguration.create();
    Properties properties = propertiesProvider.get();
    config.set("hbase.zookeeper.quorum",
        properties.getProperty("app.zookeeper.quorum"));
    config.set("hbase.zookeeper.property.clientPort",
        properties.getProperty("app.zookeeper.client.port"));
    config.set("zookeeper.znode.parent",
        properties.getProperty("app.zookeeper.znode.hbase"));
    return ConnectionFactory.createConnection(config);
  }

  protected Table getTable(Connection connection, String name)
      throws IOException {

    TableName tableName = TableName.valueOf(name);
    return connection.getTable(tableName);
  }
}
