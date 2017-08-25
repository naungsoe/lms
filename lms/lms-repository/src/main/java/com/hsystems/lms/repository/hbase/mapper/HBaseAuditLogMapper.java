package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.ActionType;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseAuditLogMapper extends HBaseMapper<AuditLog> {

  @Override
  public List<AuditLog> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<AuditLog> auditLogs = new ArrayList<>();
    results.forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        long timestamp = mutationOptional.get().getTimestamp();
        AuditLog auditLog = getEntity(result, timestamp);
        auditLogs.add(auditLog);
      }
    });
    return auditLogs;
  }

  private AuditLog getEntity(Result result, long timestamp) {
    String row = Bytes.toString(result.getRow());
    int endIndex = row.indexOf(Constants.SEPARATOR);

    String id = (endIndex == -1) ? row : row.substring(0, endIndex);
    EntityType entityType = getEntityType(result, timestamp);
    User actionBy = new User(
        getId(result, timestamp),
        getFirstName(result, timestamp),
        getLastName(result, timestamp)
    );

    ActionType actionType = getActionType(result, timestamp);
    long actionTimestamp = (endIndex == -1)
        ? getTimestamp(result, timestamp)
        : Long.parseLong(row.substring(endIndex));

    return new AuditLog(
        id,
        entityType,
        actionBy,
        actionType,
        actionTimestamp
    );
  }

  @Override
  public AuditLog getEntity(List<Result> results) {
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
