package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.SignInLog;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseSignInLogMapper extends HBaseMapper<SignInLog> {

  @Override
  public List<SignInLog> getEntities(
      List<Result> results, List<Mutation> list) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<SignInLog> signInLogs = new ArrayList<>();
    results.forEach(result -> {
      SignInLog signInLog = getEntity(result);
      signInLogs.add(signInLog);
    });
    return signInLogs;
  }

  private SignInLog getEntity(Result result) {
    String id = Bytes.toString(result.getRow());
    String account = getAccount(result, 0);
    String sessionId = getSessionId(result, 0);
    String ipAddress = getIpAddress(result, 0);
    LocalDateTime dateTime = getDateTime(result, 0);
    int fails = getFails(result, 0);
    return new SignInLog(
        id,
        account,
        sessionId,
        ipAddress,
        dateTime,
        fails
    );
  }

  @Override
  public SignInLog getEntity(List<Result> results) {
    Result mainResult = results.get(0);
    return getEntity(mainResult);
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