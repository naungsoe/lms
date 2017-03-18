package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.ActionType;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.MutateLog;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseMutateLogMapper extends HBaseMapper<MutateLog> {

  private static final String KEY_PATTERN = "^([A-Za-z0-9]*)_([A-Za-z0-9]*)$";

  private static final String KEY_FORMAT = "%s_%s";

  public String getId(EntityType type, String id) {
    return String.format(HBaseMutateLogMapper.KEY_FORMAT, type, id);
  }

  public MutateLog getEntity(Result result) {
    return getEntity(Arrays.asList(result));
  }

  @Override
  public MutateLog getEntity(List<Result> results) {
    Result mainResult = results.get(0);
    String row = Bytes.toString(mainResult.getRow());
    Pattern pattern = Pattern.compile(KEY_PATTERN);
    Matcher matcher = pattern.matcher(row);
    String id = "";
    EntityType type = EntityType.UNKNOWN;

    if (matcher.matches()) {
      id = matcher.group(2);
      type = EntityType.valueOf(matcher.group(1));
    }

    ActionType actionType = getAction(mainResult, ActionType.class);
    long timestamp = getTimestamp(mainResult);
    return new MutateLog(id, type, actionType, timestamp);
  }

  @Override
  public List<Put> getPuts(MutateLog entity, long timestamp) {
    List<Put> puts = new ArrayList<>();
    String id = String.format(KEY_FORMAT, entity.getType(), entity.getId());
    byte[] row = Bytes.toBytes(id);
    Put logPut = new Put(row, timestamp);
    addTypeColumn(logPut, entity.getType());
    addActionColumn(logPut, entity.getActionType());
    addTimestampColumn(logPut, timestamp);
    puts.add(logPut);
    return puts;
  }

  @Override
  public List<Delete> getDeletes(MutateLog entity, long timestamp) {
    List<Delete> deletes = new ArrayList<>();
    String id = String.format(KEY_FORMAT, entity.getType(), entity.getId());
    byte[] row = Bytes.toBytes(id);
    Delete delete = new Delete(row, timestamp);
    deletes.add(delete);
    return deletes;
  }
}
