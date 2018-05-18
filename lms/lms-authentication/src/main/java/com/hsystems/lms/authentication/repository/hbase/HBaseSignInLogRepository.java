package com.hsystems.lms.authentication.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.authentication.repository.entity.SignInLog;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseSignInLogRepository
    implements Repository<SignInLog> {

  private static final String SIGNIN_LOG_TABLE = "lms:signinlogs";

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseSignInLogMapper logMapper;

  @Inject
  HBaseSignInLogRepository(HBaseClient hbaseClient) {
    this.hbaseClient = hbaseClient;
    this.logMapper = new HBaseSignInLogMapper();
  }

  @Override
  public Optional<SignInLog> findBy(String id)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));
    get.setMaxVersions(MAX_VERSIONS);

    Result result = hbaseClient.get(get, SIGNIN_LOG_TABLE);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(logMapper.from(result));
  }

  @Override
  public void add(SignInLog entity)
      throws IOException {

  }

  @Override
  public void update(SignInLog entity)
      throws IOException {

  }

  @Override
  public void remove(SignInLog entity)
      throws IOException {

  }
}