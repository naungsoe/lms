package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.Action;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.MutateLog;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseMutateLogMapper extends HBaseMapper<MutateLog> {

  @Override
  public MutateLog getEntity(List<Result> results) {
    Result mainResult = results.get(0);
    String id = Bytes.toString(mainResult.getRow());
    EntityType type = getType(mainResult, EntityType.class);
    Action action = getAction(mainResult, Action.class);
    long timestamp = getTimestamp(mainResult);
    return new MutateLog(id, type, action, timestamp);
  }

  @Override
  public List<Put> getPuts(MutateLog entity, long timestamp) {
    List<Put> puts = new ArrayList<>();

    byte[] row = Bytes.toBytes(entity.getId());
    Put logPut = new Put(row, timestamp);
    addTypeColumn(logPut, entity.getType());
    addActionColumn(logPut, entity.getAction());
    addTimestampColumn(logPut, timestamp);
    puts.add(logPut);

    return puts;
  }

  @Override
  public List<Delete> getDeletes(MutateLog entity, long timestamp) {
    List<Delete> deletes = new ArrayList<>();

    byte[] row = Bytes.toBytes(entity.getId());
    Delete delete = new Delete(row, timestamp);
    deletes.add(delete);

    return deletes;
  }
}
