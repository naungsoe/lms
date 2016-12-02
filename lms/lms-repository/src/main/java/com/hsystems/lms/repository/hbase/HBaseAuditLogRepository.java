package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.Action;
import com.hsystems.lms.common.EntityType;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseAuditLogRepository
    extends HBaseRepository implements AuditLogRepository {

  private final HBaseClient client;

  @Inject
  HBaseAuditLogRepository(HBaseClient client) {
    this.client = client;
  }

  public List<AuditLog> findAllBy(String id)
      throws RepositoryException {

    try {
      Scan scan = getRowFilterScan(id);
      List<Result> results = client.scan(scan, Constants.TABLE_AUDIT_LOG);

      if (results.isEmpty()) {
        return Collections.emptyList();
      }

      List<AuditLog> auditLogs = new ArrayList<>();

      for (Result result : results) {
        String rowKey = Bytes.toString(result.getRow());
        auditLogs.add(getAuditLog(rowKey, result));
      }

      return auditLogs;

    } catch (IOException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {

      throw new RepositoryException("error retrieving auditlog", e);
    }
  }

  protected AuditLog getAuditLog(String rowKey, Result result)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    AuditLog auditLog = (AuditLog) ReflectionUtils.getInstance(AuditLog.class);

    if (rowKey.contains(Constants.SEPARATOR)) {
      String[] pair = rowKey.split(Constants.SEPARATOR);
      ReflectionUtils.setValue(auditLog, Constants.FIELD_ID, pair[0]);
      ReflectionUtils.setValue(auditLog, Constants.FIELD_TIMESTAMP, pair[1]);

    } else {
      ReflectionUtils.setValue(auditLog, Constants.FIELD_ID,
          getString(result, Constants.FAMILY_DATA,
              Constants.IDENTIFIER_ID));
      ReflectionUtils.setValue(auditLog, Constants.FIELD_TIMESTAMP,
          getLong(result, Constants.FAMILY_DATA,
              Constants.IDENTIFIER_TIMESTAMP));
    }

    ReflectionUtils.setValue(auditLog, Constants.FIELD_TYPE,
        getEnum(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_TYPE, EntityType.class));
    ReflectionUtils.setValue(auditLog, Constants.FIELD_USER,
        getUser(rowKey, result));
    ReflectionUtils.setValue(auditLog, Constants.FIELD_ACTION,
        getEnum(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_ACTION, Action.class));
    return auditLog;
  }

  public Optional<AuditLog> findLastestLogBy(String id)
      throws RepositoryException {

    try {
      Get get = new Get(Bytes.toBytes(id));
      Result result = client.get(get, Constants.TABLE_AUDIT_LOG);

      if (result.isEmpty()) {
        return Optional.empty();
      }

      String rowKey = Bytes.toString(result.getRow());
      return Optional.of(getAuditLog(rowKey, result));

    } catch (IOException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {
      throw new RepositoryException("error retrieving auditlog", e);
    }
  }

  public void save(AuditLog auditLog)
      throws RepositoryException {

    try {
      Put put = new Put(Bytes.toBytes(auditLog.getEntityId()));
      byte[] type = Bytes.toBytes(auditLog.getEntityType().toString());
      put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_TYPE, type);
      client.put(put, Constants.TABLE_AUDIT_LOG);

    } catch (IOException e) {
      throw new RepositoryException("error saving auditlog", e);
    }
  }
}
