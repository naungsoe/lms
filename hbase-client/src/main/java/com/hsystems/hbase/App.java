package com.hsystems.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Iterator;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) {
    Configuration configuration = HBaseConfiguration.create();

    try (Connection connection
             = ConnectionFactory.createConnection(configuration)) {

      Table table = connection.getTable(TableName.valueOf("users"));
      Scan scan = new Scan(Bytes.toBytes("admin"));
      RegexStringComparator comparator
          = new RegexStringComparator("^admin[_0-9a-zA-Z]*$");
      RowFilter filter = new RowFilter(
          CompareFilter.CompareOp.EQUAL, comparator);
      scan.setFilter(filter);
      ResultScanner scanner = table.getScanner(scan);
      Iterator<Result> iterator = scanner.iterator();

      while (iterator.hasNext()) {
        Result result = iterator.next();
        System.out.println(Bytes.toString(result.getRow()));
      }
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}
