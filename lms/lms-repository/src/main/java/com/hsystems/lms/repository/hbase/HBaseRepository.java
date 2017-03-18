package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.common.ActionType;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.MutateLog;
import com.hsystems.lms.repository.entity.User;

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

  protected <T extends Entity> MutateLog getMutateLog(
      T entity, ActionType actionType, long timestamp) {

    return new MutateLog(
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
