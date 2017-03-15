package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.ActionType;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseAuditLogMapper extends HBaseMapper<AuditLog> {

  public AuditLog getEntity(Result result) {
    return getEntity(Arrays.asList(result));
  }

  @Override
  public AuditLog getEntity(List<Result> results) {
    Result mainResult = results.get(0);
    String row = Bytes.toString(mainResult.getRow());
    int endIndex = row.indexOf(Constants.SEPARATOR);

    String id = (endIndex == -1) ? row : row.substring(0, endIndex);
    EntityType type = getType(mainResult, EntityType.class);
    User user = new User(getId(
        mainResult),
        getFirstName(mainResult),
        getLastName(mainResult)
    );
    ActionType actionType = getAction(mainResult, ActionType.class);
    long timestamp = (endIndex == -1)
        ? getTimestamp(mainResult) : Long.parseLong(row.substring(endIndex));

    return new AuditLog(
        id,
        type,
        user,
        actionType,
        timestamp
    );
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
