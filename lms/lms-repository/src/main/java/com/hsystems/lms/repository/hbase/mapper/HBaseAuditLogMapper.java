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
    Result result = results.get(0);
    String row = Bytes.toString(result.getRow());
    int endIndex = row.indexOf(Constants.SEPARATOR);
    String id = endIndex > 0
        ? row.substring(0, endIndex) : row;
    long timestamp = endIndex > 0
        ? Long.parseLong(row.substring(endIndex))
        : getTimestamp(result);

    EntityType type = getType(result, EntityType.class);
    Action action = getAction(result, Action.class);

    User user = new User(
        getId(result),
        getFirstName(result),
        getLastName(result)
    );

    return new AuditLog(
        id,
        type,
        user,
        timestamp,
        action
    );
  }

  @Override
  public List<Put> getPuts(AuditLog entity, long timestamp) {
    List<Put> logPuts = new ArrayList<>();
    Put latestLogPut = new Put(Bytes.toBytes(entity.getId()));
    addTypeColumn(latestLogPut, entity.getType());
    addIdColumn(latestLogPut, entity.getUser().getId());
    addFirstNameColumn(latestLogPut, entity.getUser().getFirstName());
    addLastNameColumn(latestLogPut, entity.getUser().getLastName());
    addTimestampColumn(latestLogPut, timestamp);
    addActionColumn(latestLogPut, entity.getAction());
    logPuts.add(latestLogPut);

    String rowKey = entity.getId()
        + Constants.SEPARATOR + timestamp;
    Put logPut = new Put(Bytes.toBytes(rowKey));
    addTypeColumn(logPut, entity.getType());
    addIdColumn(logPut, entity.getUser().getId());
    addFirstNameColumn(logPut, entity.getUser().getFirstName());
    addLastNameColumn(logPut, entity.getUser().getLastName());
    addActionColumn(logPut, entity.getAction());
    logPuts.add(logPut);
    return logPuts;
  }

  @Override
  public List<Delete> getDeletes(AuditLog entity, long timestamp) {
    return new ArrayList<>();
  }
}
