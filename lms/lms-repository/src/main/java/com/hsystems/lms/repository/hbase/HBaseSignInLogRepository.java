package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.SignInLogRepository;
import com.hsystems.lms.repository.entity.SignInLog;
import com.hsystems.lms.repository.hbase.mapper.HBaseSignInLogMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseSignInLogRepository
    extends HBaseRepository implements SignInLogRepository {

  private final HBaseClient client;

  private final HBaseSignInLogMapper mapper;

  @Inject
  HBaseSignInLogRepository(
      HBaseClient client,
      HBaseSignInLogMapper mapper) {

    this.client = client;
    this.mapper = mapper;
  }

  @Override
  public Optional<SignInLog> findBy(String id)
      throws IOException {

    return findBy(id, Long.MIN_VALUE);
  }

  @Override
  public Optional<SignInLog> findBy(String id, long timestamp)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));

    if (timestamp != Long.MIN_VALUE) {
      get.setTimeStamp(timestamp);
    }

    Result result = client.get(get, Constants.TABLE_SIGNIN_LOGS);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    SignInLog signInLog = mapper.getEntity(Arrays.asList(result));
    return Optional.of(signInLog);
  }

  @Override
  public void save(SignInLog signInLog, long timestamp)
      throws IOException {

    List<Put> puts = mapper.getPuts(signInLog, timestamp);
    client.put(puts, Constants.TABLE_SIGNIN_LOGS);
  }

  @Override
  public void delete(SignInLog signInLog, long timestamp)
      throws IOException {

    List<Delete> deletes = mapper.getDeletes(signInLog, timestamp);
    client.delete(deletes, Constants.TABLE_SIGNIN_LOGS);
  }
}
