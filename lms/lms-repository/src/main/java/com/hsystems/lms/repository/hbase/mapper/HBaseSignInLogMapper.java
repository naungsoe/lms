package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.SignInLog;

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
  public List<SignInLog> getEntities(List<Result> results) {
    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<SignInLog> logs = new ArrayList<>();
    results.forEach(result -> {
      Optional<SignInLog> logOptional = getEntity(result);

      if (logOptional.isPresent()) {
        logs.add(logOptional.get());
      }
    });

    return logs;
  }

  private Optional<SignInLog> getEntity(Result result) {
    String id = Bytes.toString(result.getRow());
    String account = getAccount(result);
    String sessionId = getSessionId(result);
    String ipAddress = getIpAddress(result);
    LocalDateTime dateTime = getDateTime(result);
    int fails = getFails(result);

    SignInLog log = new SignInLog(
        id,
        account,
        sessionId,
        ipAddress,
        dateTime,
        fails
    );
    return Optional.of(log);
  }

  @Override
  public Optional<SignInLog> getEntity(List<Result> results) {
    Result mainResult = results.get(0);
    return getEntity(mainResult);
  }
}