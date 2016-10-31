package com.hbase.jersey.repository.hbase;

import com.hbase.jersey.repository.UserRepository;

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
 * Created by naungsoe on 8/8/16.
 */
public final class HBaseUserRepository
    extends HBaseRepository implements UserRepository {

  public String findBy(String key) {
    Configuration configuration = HBaseConfiguration.create();
    try (Connection connection
             = ConnectionFactory.createConnection(configuration)) {

      Table table = connection.getTable(TableName.valueOf("users"));

      Scan scan = new Scan(Bytes.toBytes(key));
      RegexStringComparator comparator
          = new RegexStringComparator("^admin[_0-9a-zA-Z]*$");
      RowFilter filter = new RowFilter(
          CompareFilter.CompareOp.EQUAL, comparator);
      scan.setFilter(filter);

      ResultScanner scanner = table.getScanner(scan);
      Iterator<Result> iterator = scanner.iterator();
      StringBuilder keys = new StringBuilder();

      while (iterator.hasNext()) {
        Result result = iterator.next();
        keys.append(Bytes.toString(result.getRow()));
      }
      return keys.toString();
    } catch (IOException e) {
      return "";
    }
  }
}
