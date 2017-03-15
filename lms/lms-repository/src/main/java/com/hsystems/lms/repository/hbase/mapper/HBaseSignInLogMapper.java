package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.SignInLog;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseSignInLogMapper extends HBaseMapper<SignInLog> {

  public SignInLog getEntity(Result result) {
    return getEntity(Arrays.asList(result));
  }

  @Override
  public SignInLog getEntity(List<Result> results) {
    Result mainResult = results.get(0);
    String id = Bytes.toString(mainResult.getRow());
    String sessionId = getSessionId(mainResult);
    String ipAddress = getIpAddress(mainResult);
    LocalDateTime dateTime = getDateTime(mainResult);
    int fails = getFails(mainResult);
    return new SignInLog(id, sessionId, ipAddress, dateTime, fails);
  }

  @Override
  public List<Put> getPuts(SignInLog entity, long timestamp) {
    List<Put> puts = new ArrayList<>();
    byte[] row = Bytes.toBytes(entity.getId());
    Put put = new Put(row, timestamp);
    addSessionIdColumn(put, entity.getSessionId());
    addIpAddressColumn(put, entity.getIpAddress());
    addDateTimeColumn(put, entity.getDateTime());
    addFailsColumn(put, entity.getFails());
    puts.add(put);
    return puts;
  }

  @Override
  public List<Delete> getDeletes(SignInLog entity, long timestamp) {
    List<Delete> deletes = new ArrayList<>();

    byte[] row = Bytes.toBytes(entity.getId());
    Delete delete = new Delete(row, timestamp);
    deletes.add(delete);
    return deletes;
  }
}
