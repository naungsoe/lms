package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.SignInLogRepository;
import com.hsystems.lms.repository.entity.SignInLog;
import com.hsystems.lms.repository.hbase.mapper.HBaseSignInLogMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseSignInLogRepository
    extends HBaseRepository implements SignInLogRepository {

  private final HBaseClient client;

  private final HBaseSignInLogMapper signInLogMapper;

  @Inject
  HBaseSignInLogRepository(
      HBaseClient client,
      HBaseSignInLogMapper signInLogMapper) {

    this.client = client;
    this.signInLogMapper = signInLogMapper;
  }

  @Override
  public Optional<SignInLog> findBy(String id)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));
    Result result = client.get(get, SignInLog.class);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    SignInLog signInLog = signInLogMapper.getEntity(result);
    return Optional.of(signInLog);
  }

  @Override
  public void save(SignInLog signInLog)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    List<Put> puts = signInLogMapper.getPuts(signInLog, timestamp);
    client.put(puts, SignInLog.class);
  }

  @Override
  public void delete(SignInLog signInLog)
      throws IOException {

  }
}
