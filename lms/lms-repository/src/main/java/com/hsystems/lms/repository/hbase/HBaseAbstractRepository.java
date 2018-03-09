package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.repository.entity.ActionType;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.PermissionSet;
import com.hsystems.lms.repository.entity.Resource;
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.entity.User;

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

  private static final String FORMAT_SCAN_KEY = "^%s[A-Za-z0-9_]*%s$";
  private static final String FORMAT_EXCLUSIVE_START_KEY = "%s0";
  private static final String FORMAT_INCLUSIVE_STOP_KEY = "%s~";

  protected static final int MAX_VERSIONS = 3;

  protected Scan getRowKeyFilterScan(String prefix)
      throws IOException {

    return getRowKeyFilterScan(prefix, "");
  }

  protected Scan getRowKeyFilterScan(String prefix, String suffix)
      throws IOException {

    String keyRegex = String.format(FORMAT_SCAN_KEY, prefix, suffix);
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
    String keyRegex = String.format(FORMAT_SCAN_KEY, prefix, suffix);
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
    return String.format(FORMAT_EXCLUSIVE_START_KEY, startRowKey);
  }

  protected String getInclusiveStopRowKey(String stopRowKey) {
    return String.format(FORMAT_INCLUSIVE_STOP_KEY, stopRowKey);
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

  protected <T extends Entity> Mutation getMutation(
      T entity, ActionType actionType, long timestamp) {

    return new Mutation(
        entity.getId(),
        getEntityType(entity),
        actionType,
        timestamp
    );
  }

  private <T extends Entity> EntityType getEntityType(T entity) {
    String name = entity.getClass().getSimpleName();
    return Enum.valueOf(EntityType.class, name);
  }

  protected List<String> getResultRowKeys(List<Result> results) {
    List<String> rowKeys = new ArrayList<>();
    results.forEach(result -> {
      String rowKey = Bytes.toString(result.getRow());
      rowKeys.add(rowKey);
    });
    return rowKeys;
  }

  protected List<String> getPutRowKeys(List<Put> puts) {
    List<String> rowKeys = new ArrayList<>();
    puts.forEach(put -> {
      String rowKey = Bytes.toString(put.getRow());
      rowKeys.add(rowKey);
    });
    return rowKeys;
  }

  protected <T extends Entity> AuditLog getAuditLog(
      T entity, User actionBy, ActionType actionType, long timestamp) {

    return new AuditLog(
        entity.getId(),
        getEntityType(entity),
        actionBy,
        actionType,
        timestamp
    );
  }

  protected void populatePermissionSets(Resource resource, ShareLog shareLog) {
    Enumeration<PermissionSet> enumeration = shareLog.getPermissionSets();

    while (enumeration.hasMoreElements()) {
      PermissionSet element = enumeration.nextElement();
      resource.addPermissionSet(element);
    }
  }
}
