package com.hsystems.lms.repository.hbase;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by naungsoe on 13/10/16.
 */
public abstract class HBaseRepository {

  private static final String SCAN_KEY_FORMAT = "^%s[A-Za-z0-9_]*%s$";

  protected Scan getRowKeyFilterScan(String prefix)
      throws IOException {

    return getRowKeyFilterScan(prefix, "");
  }

  protected Scan getRowKeyFilterScan(String prefix, String suffix)
      throws IOException {

    String keyRegex = String.format(SCAN_KEY_FORMAT, prefix, suffix);
    Scan scan = new Scan(Bytes.toBytes(prefix));
    RegexStringComparator comparator = new RegexStringComparator(keyRegex);
    RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, comparator);
    scan.setFilter(filter);
    return scan;
  }
}
