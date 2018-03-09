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
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseSignInLogMapper extends HBaseAbstractMapper<SignInLog> {

  @Override
  public List<SignInLog> getEntities(
      List<Result> results, List<Mutation> list) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<SignInLog> signInLogs = new ArrayList<>();
    results.forEach(result -> {
      Optional<SignInLog> signInLogOptional = getEntity(result);

      if (signInLogOptional.isPresent()) {
        signInLogs.add(signInLogOptional.get());
      }
    });
    return signInLogs;
  }

  private Optional<SignInLog> getEntity(Result result) {
    String id = Bytes.toString(result.getRow());
    String account = getAccount(result, 0);
    String sessionId = getSessionId(result, 0);
    String ipAddress = getIpAddress(result, 0);
    LocalDateTime dateTime = getDateTime(result, 0);
    int fails = getFails(result, 0);

    SignInLog signInLog = new SignInLog(
        id,
        account,
        sessionId,
        ipAddress,
        dateTime,
        fails
    );
    return Optional.of(signInLog);
  }

  @Override
  public Optional<SignInLog> getEntity(List<Result> results) {
    Result mainResult = results.get(0);
    return getEntity(mainResult);
  }

  @Override
  public List<Put> getPuts(SignInLog entity, long timestamp) {
    List<Put> puts = new ArrayList<>();
    byte[] rowKey = Bytes.toBytes(entity.getId());
    Put put = new Put(rowKey, timestamp);
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
    byte[] rowKey = Bytes.toBytes(entity.getId());
    Delete delete = new Delete(rowKey, timestamp);
    deletes.add(delete);
    return deletes;
  }
}