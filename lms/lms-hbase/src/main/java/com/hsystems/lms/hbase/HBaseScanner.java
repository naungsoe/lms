package com.hsystems.lms.hbase;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;

import java.io.IOException;

public final class HBaseScanner {

  private static final String SCAN_KEY_FORMAT = "^%s[A-Za-z0-9_]*%s$";
  private static final String EXCLUSIVE_START_KEY_FORMAT = "%s0";
  private static final String INCLUSIVE_STOP_KEY_FORMAT = "%s~";

  public static final int MAX_VERSIONS = 1;

  public static Scan createRowKeyFilter(String prefix)
      throws IOException {

    return createRowKeyFilter(prefix, "");
  }

  public static Scan createRowKeyFilter(String prefix, String suffix)
      throws IOException {

    String keyRegex = String.format(SCAN_KEY_FORMAT, prefix, suffix);
    RegexStringComparator comparator = new RegexStringComparator(keyRegex);
    RowFilter rowFilter = new RowFilter(
        CompareFilter.CompareOp.EQUAL, comparator);

    Scan scan = new Scan();
    scan.setFilter(rowFilter);
    return scan;
  }

  protected Scan createRowKeyOnlyFilter(String prefix)
      throws IOException {

    return createRowKeyOnlyFilter(prefix, "");
  }

  protected Scan createRowKeyOnlyFilter(String prefix, String suffix)
      throws IOException {

    FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
    String keyRegex = String.format(SCAN_KEY_FORMAT, prefix, suffix);
    RegexStringComparator comparator = new RegexStringComparator(keyRegex);
    RowFilter rowFilter = new RowFilter(
        CompareFilter.CompareOp.EQUAL, comparator);
    filterList.addFilter(rowFilter);

    FirstKeyOnlyFilter firstKeyOnlyFilter = new FirstKeyOnlyFilter();
    filterList.addFilter(firstKeyOnlyFilter);

    KeyOnlyFilter keyOnlyFilter = new KeyOnlyFilter();
    filterList.addFilter(keyOnlyFilter);

    Scan scan = new Scan();
    scan.setFilter(filterList);
    return scan;
  }

  protected Scan createColumnValueFilter(
      byte[] family, byte[] qualifier, byte[] value)
      throws IOException {

    FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
    BinaryComparator familyComparator = new BinaryComparator(family);
    FamilyFilter familyFilter = new FamilyFilter(
        CompareFilter.CompareOp.EQUAL, familyComparator);
    filterList.addFilter(familyFilter);

    BinaryComparator qualifierComparator = new BinaryComparator(qualifier);
    QualifierFilter qualifierFilter = new QualifierFilter(
        CompareFilter.CompareOp.EQUAL, qualifierComparator);
    filterList.addFilter(qualifierFilter);

    BinaryComparator valueComparator = new BinaryComparator(value);
    ValueFilter valueFilter = new ValueFilter(
        CompareFilter.CompareOp.EQUAL, valueComparator);
    filterList.addFilter(valueFilter);

    Scan scan = new Scan();
    scan.setFilter(filterList);
    return scan;
  }
}