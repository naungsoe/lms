package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.ActionType;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseAuditLogMapper extends HBaseAbstractMapper<AuditLog> {

  @Override
  public List<AuditLog> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<AuditLog> auditLogs = new ArrayList<>();
    results.forEach(result -> {
      Optional<AuditLog> logOptional = getEntity(Arrays.asList(result));

      if (logOptional.isPresent()) {
        auditLogs.add(logOptional.get());
      }
    });

    return auditLogs;
  }

  private final Optional<AuditLog> getEntity(Result result, long timestamp) {
    String rowKey = Bytes.toString(result.getRow());
    int endIndex = rowKey.indexOf(Constants.SEPARATOR);

    String id = (endIndex == -1) ? rowKey : rowKey.substring(0, endIndex);
    EntityType entityType = getEntityType(result, timestamp);
    User actionBy = new User.Builder(
        getId(result, timestamp),
        getFirstName(result, timestamp),
        getLastName(result, timestamp)
    ).build();

    ActionType actionType = getActionType(result, timestamp);
    long actionTimestamp = (endIndex == -1)
        ? getTimestamp(result, timestamp)
        : Long.parseLong(rowKey.substring(endIndex));

    AuditLog auditLog = new AuditLog(
        id,
        entityType,
        actionBy,
        actionType,
        actionTimestamp
    );
    return Optional.of(auditLog);
  }

  @Override
  public Optional<AuditLog> getEntity(List<Result> results) {
    Result mainResult = results.get(0);
    return getEntity(mainResult, 0);
  }

  @Override
  public List<Put> getPuts(AuditLog entity, long timestamp) {
    return Collections.emptyList();
  }

  @Override
  public List<Delete> getDeletes(AuditLog entity, long timestamp) {
    return Collections.emptyList();
  }
}
