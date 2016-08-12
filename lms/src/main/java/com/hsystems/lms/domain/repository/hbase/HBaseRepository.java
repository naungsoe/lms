package com.hsystems.lms.domain.repository.hbase;

import com.hsystems.lms.domain.model.hbase.NullResult;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

/**
 * Created by administrator on 8/8/16.
 */
public class HBaseRepository {

  private final String tableName;

  public HBaseRepository(String tableName) {
    this.tableName = tableName;
  }

  public String getTableName() {
    return tableName;
  }

  public ResultScanner scanResults(Scan scan) throws IOException {
    Connection connection = getConnection();
    Table table = connection.getTable(TableName.valueOf(tableName));
    ResultScanner results;

    try {
      results = table.getScanner(scan);
    } finally {
      table.close();
      connection.close();
    }
    return results;
  }

  public Result getResult(Get get) throws IOException {
    Connection connection = getConnection();
    Table table = connection.getTable(TableName.valueOf(tableName));
    Result result;

    try {
      result = table.get(get);
    } finally {
      table.close();
      connection.close();
    }

    if (result == null) {
      result = new NullResult();
    }
    return result;
  }

  protected Connection getConnection() throws IOException {
    Configuration config = HBaseConfiguration.create();
    config.set("hbase.zookeeper.quorum", "vagrant-ubuntu-trusty-64");
    config.set("hbase.zookeeper.property.clientPort", "2181");
    return ConnectionFactory.createConnection(config);
  }
}
