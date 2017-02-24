package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.SignInLog;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseSignInLogMapper extends HBaseMapper<SignInLog> {

  @Override
  public SignInLog getEntity(List<Result> results) {
    Result mainResult = results.get(0);
    String sessionId = Bytes.toString(mainResult.getRow());
    String id = getId(mainResult);
    String ipAddress = getIpAddress(mainResult);
    LocalDateTime dateTime = getDateTime(mainResult);
    return new SignInLog(id, sessionId, ipAddress, dateTime);
  }

  @Override
  public List<Put> getPuts(SignInLog entity, long timestamp) {
    List<Put> puts = new ArrayList<>();

    byte[] latestLogRow = Bytes.toBytes(entity.getId());
    Put latestLogPut = new Put(latestLogRow, timestamp);
    addIpAddressColumn(latestLogPut, entity.getIpAddress());
    addDateTimeColumn(latestLogPut, entity.getDateTime());
    puts.add(latestLogPut);

    String logId = String.format("%s%s%s", entity.getId(),
        Constants.SEPARATOR, timestamp);
    byte[] logRow = Bytes.toBytes(logId);
    Put logPut = new Put(logRow, timestamp);
    addIpAddressColumn(logPut, entity.getIpAddress());
    addDateTimeColumn(logPut, entity.getDateTime());
    puts.add(logPut);

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
