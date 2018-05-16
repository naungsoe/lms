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
import org.apache.hadoop.hbase.util.Bytes;

public final class HBaseScanFactory {

  private static final String SCAN_KEY_FORMAT = "^%s[A-Za-z0-9_]*%s$";
  private static final String EXCLUSIVE_START_KEY_FORMAT = "%s0";
  private static final String INCLUSIVE_STOP_KEY_FORMAT = "%s~";

  public static Scan createExclStartRowKeyScan(String rowKey) {
    String startRowKey = String.format(EXCLUSIVE_START_KEY_FORMAT, rowKey);
    Scan scan = new Scan(Bytes.toBytes(startRowKey));
    return scan;
  }

  public static Scan createInclStopRowKeyScan(String rowKey) {
    String startRowKey = String.format(INCLUSIVE_STOP_KEY_FORMAT, rowKey);
    Scan scan = new Scan(Bytes.toBytes(startRowKey));
    return scan;
  }

  public static Scan createRowKeyFilterScan(String prefix) {
    return createRowKeyFilterScan(prefix, "");
  }

  public static Scan createRowKeyFilterScan(String prefix, String suffix) {

    String keyRegex = String.format(SCAN_KEY_FORMAT, prefix, suffix);
    RegexStringComparator comparator = new RegexStringComparator(keyRegex);
    RowFilter rowFilter = new RowFilter(
        CompareFilter.CompareOp.EQUAL, comparator);

    Scan scan = new Scan();
    scan.setFilter(rowFilter);
    return scan;
  }

  protected Scan createRowKeyOnlyFilterScan(String prefix) {
    return createRowKeyOnlyFilterScan(prefix, "");
  }

  protected Scan createRowKeyOnlyFilterScan(String prefix, String suffix) {
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

  public Scan createColumnValueFilterScan(
      byte[] family, byte[] qualifier, byte[] value) {

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