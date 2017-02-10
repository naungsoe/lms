package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.Action;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseAuditLogMapper extends HBaseMapper<AuditLog> {

  @Override
  public AuditLog getEntity(List<Result> results) {
    Result mainResult = results.get(0);
    String row = Bytes.toString(mainResult.getRow());
    int endIndex = row.indexOf(Constants.SEPARATOR);

    String id = endIndex > 0
        ? row.substring(0, endIndex) : row;
    EntityType type = getType(mainResult, EntityType.class);
    User user = new User(
        getId(mainResult),
        getFirstName(mainResult),
        getLastName(mainResult)
    );
    Action action = getAction(mainResult, Action.class);
    long timestamp = endIndex > 0
        ? Long.parseLong(row.substring(endIndex))
        : getTimestamp(mainResult);

    return new AuditLog(
        id,
        type,
        user,
        action,
        timestamp
    );
  }

  @Override
  public List<Put> getPuts(AuditLog entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(AuditLog entity, long timestamp) {
    return new ArrayList<>();
  }
}
