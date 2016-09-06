package com.hsystems.lms.domain.repository.hbase;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.model.hbase.NullResult;
import com.hsystems.lms.domain.repository.mapping.ColumnMap;
import com.hsystems.lms.domain.repository.mapping.DataMap;
import com.hsystems.lms.exception.ApplicationException;

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

/**
 * Created by administrator on 8/8/16.
 */
public class HBaseMapper {

  protected final DataMap dataMap;

  public HBaseMapper(Class domainClass, String tableName) {
    this.dataMap = new DataMap(domainClass, tableName);
  }

  public ResultScanner scanResults(Scan scan) throws IOException {
    Connection connection = getConnection();
    Table table = getTable(connection);
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
    Table table = getTable(connection);
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

  public void putRecord(Put put) throws IOException {
    Connection connection = getConnection();
    Table table = getTable(connection);

    try {
      table.put(put);
    } finally {
      table.close();
      connection.close();
    }
  }

  protected Connection getConnection() throws IOException {
    Configuration config = HBaseConfiguration.create();
    config.set("hbase.zookeeper.quorum", "vagrant-ubuntu-trusty-64");
    config.set("hbase.zookeeper.property.clientPort", "2181");
    return ConnectionFactory.createConnection(config);
  }

  protected Table getTable(Connection connection)
      throws IOException {

    TableName tableName = TableName.valueOf(dataMap.getTableName());
    return connection.getTable(tableName);
  }

  protected void loadFields(Object object, Result result)
      throws ApplicationException {

    for (ColumnMap columnMap : dataMap.getColumnMaps()) {
      Object columnValue = HBaseUtils.getString(result,
          columnMap.getColumnFamilyName(), columnMap.getColumnName());
      columnMap.setField(object, columnValue);
    }
  }
}
