package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.common.annotation.IndexDocument;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Permission;
import com.hsystems.lms.repository.entity.Resource;
import com.hsystems.lms.repository.entity.ShareLog;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

/**
 * Created by naungsoe on 13/10/16.
 */
public abstract class HBaseAbstractRepository {

  private static final String SCAN_KEY_FORMAT = "^%s[A-Za-z0-9_]*%s$";
  private static final String EXCLUSIVE_START_KEY_FORMAT = "%s0";
  private static final String INCLUSIVE_STOP_KEY_FORMAT = "%s~";

  protected static final int MAX_VERSIONS = 1;

  protected  <T> TableName getTableName(Class<T> type) {
    IndexDocument annotation = type.getAnnotation(IndexDocument.class);
    String namespace = annotation.namespace();
    String collection = StringUtils.isEmpty(annotation.collection())
        ? type.getSimpleName() : annotation.collection();
    String tableName = StringUtils.isEmpty(namespace)
        ? collection : String.format("%s:%s", namespace, collection);
    return TableName.valueOf(tableName);
  }

  protected Scan getRowKeyFilterScan(String prefix)
      throws IOException {

    return getRowKeyFilterScan(prefix, "");
  }

  protected Scan getRowKeyFilterScan(String prefix, String suffix)
      throws IOException {

    String keyRegex = String.format(SCAN_KEY_FORMAT, prefix, suffix);
    RegexStringComparator comparator = new RegexStringComparator(keyRegex);
    RowFilter rowFilter = new RowFilter(
        CompareFilter.CompareOp.EQUAL, comparator);

    Scan scan = new Scan();
    scan.setFilter(rowFilter);
    return scan;
  }

  protected Scan getRowKeyOnlyFilterScan(String prefix)
      throws IOException {

    return getRowKeyOnlyFilterScan(prefix, "");
  }

  protected Scan getRowKeyOnlyFilterScan(String prefix, String suffix)
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

  protected String getExclusiveStartRowKey(String startRowKey) {
    return String.format(EXCLUSIVE_START_KEY_FORMAT, startRowKey);
  }

  protected String getInclusiveStopRowKey(String stopRowKey) {
    return String.format(INCLUSIVE_STOP_KEY_FORMAT, stopRowKey);
  }

  protected Scan getColumnValueFilterScan(
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

  protected Scan getRowKeysFilterScan(Set<String> rowKeys)
      throws IOException {

    List<MultiRowRangeFilter.RowRange> ranges = new ArrayList<>();

    for (String rowKey : rowKeys) {
      String stopRowKey = getInclusiveStopRowKey(rowKey);
      ranges.add(new MultiRowRangeFilter.RowRange(
          rowKey, true, stopRowKey, true));
    }

    Filter filter = new MultiRowRangeFilter(ranges);
    Scan scan = new Scan();
    scan.setFilter(filter);
    return scan;
  }

  protected List<String> getUnusedRowKeys(
      List<Result> results, List<Put> puts) {

    List<String> rowKeys = getResultRowKeys(results);
    List<String> savedRowKeys = getPutRowKeys(puts);
    List<String> unusedRowKeys = new ArrayList<>();
    rowKeys.forEach(rowKey -> {
      if (!savedRowKeys.contains(rowKey)) {
        unusedRowKeys.add(rowKey);
      }
    });

    return unusedRowKeys;
  }

  private List<String> getResultRowKeys(List<Result> results) {
    List<String> rowKeys = new ArrayList<>();
    results.forEach(result -> {
      String rowKey = Bytes.toString(result.getRow());
      rowKeys.add(rowKey);
    });

    return rowKeys;
  }

  private List<String> getPutRowKeys(List<Put> puts) {
    List<String> rowKeys = new ArrayList<>();
    puts.forEach(put -> {
      String rowKey = Bytes.toString(put.getRow());
      rowKeys.add(rowKey);
    });

    return rowKeys;
  }

  protected List<Delete> getDeletes(List<String> rowKeys) {
    List<Delete> deletes = new ArrayList<>();
    rowKeys.forEach(rowKey -> {
      byte[] deleteRowKey = Bytes.toBytes(rowKey);
      Delete delete = new Delete(deleteRowKey);
      deletes.add(delete);
    });

    return deletes;
  }

  protected void populatePermissions(Resource resource, ShareLog shareLog) {
    Enumeration<Permission> enumeration = shareLog.getPermissions();

    while (enumeration.hasMoreElements()) {
      Permission element = enumeration.nextElement();
      resource.addPermission(element);
    }
  }
}
