package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.ReflectionUtils;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;
import com.hsystems.lms.repository.model.Action;
import com.hsystems.lms.repository.model.AuditLog;
import com.hsystems.lms.repository.model.User;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseAuditLogRepository
    extends HBaseRepository implements AuditLogRepository {

  private HBaseClient client;

  @Inject
  HBaseAuditLogRepository(HBaseClient client) {
    this.client = client;
  }

  public List<AuditLog> findAllBy(String id)
      throws RepositoryException {

    try {
      Scan scan = getRowFilterScan(id);
      List<Result> results = client.scan(scan, Constants.TABLE_USERS);

      if (CollectionUtils.isEmpty(results)) {
        return Collections.EMPTY_LIST;
      }

      List<AuditLog> auditLogs = new ArrayList<>();

      for (Result result : results) {
        String rowKey = Bytes.toString(result.getRow());

        if (rowKey.contains(Constants.SEPARATOR_CREATED_BY)
            || rowKey.contains(Constants.SEPARATOR_MODIFIED_BY)) {
          auditLogs.add(getAuditLog(rowKey, result));
        }
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
    User user = (User) ReflectionUtils.getInstance(User.class);
    String id = rowKey.split(Constants.SEPARATOR_SCHOOL)[1];

    ReflectionUtils.setValue(user, Constants.FIELD_ID, id);
    ReflectionUtils.setValue(user, Constants.FIELD_NAME,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_NAME));
    ReflectionUtils.setValue(user, Constants.FIELD_ACTION,
        rowKey.contains(Constants.SEPARATOR_CREATED_BY)
            ? Action.CREATED : Action.MODIFIED);

    return auditLog;
  }
}
