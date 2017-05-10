package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.common.ActionType;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;

import java.io.IOException;

/**
 * Created by naungsoe on 13/10/16.
 */
public abstract class HBaseRepository {

  private static final String SCAN_KEY_FORMAT = "^%s[A-Za-z0-9_]*%s$";

  private static final String EXCLUSIVE_START_KEY_FORMAT = "%s0";
  private static final String INCLUSIVE_STOP_KEY_FORMAT = "%s~";

  protected static final int MAX_VERSIONS = 3;

  protected Scan getRowKeyFilterScan(String prefix)
      throws IOException {

    return getRowKeyFilterScan(prefix, "");
  }

  protected Scan getRowKeyFilterScan(String prefix, String suffix)
      throws IOException {

    Scan scan = new Scan();
    String keyRegex = String.format(SCAN_KEY_FORMAT, prefix, suffix);
    RegexStringComparator comparator = new RegexStringComparator(keyRegex);
    RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, comparator);
    scan.setFilter(filter);
    return scan;
  }

  protected String getExclusiveStartRowKey(String startRowKey) {
    return String.format(EXCLUSIVE_START_KEY_FORMAT, startRowKey);
  }

  protected String getInclusiveStopRowKey(String stopRowKey) {
    return String.format(INCLUSIVE_STOP_KEY_FORMAT, stopRowKey);
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

  protected <T extends Entity> AuditLog getAuditLog(
      T entity, User user, ActionType actionType, long timestamp) {

    return new AuditLog(
        entity.getId(),
        getEntityType(entity),
        user,
        actionType,
        timestamp
    );
  }
}
