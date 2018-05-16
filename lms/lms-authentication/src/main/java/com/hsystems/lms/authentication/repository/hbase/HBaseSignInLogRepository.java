package com.hsystems.lms.authentication.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.authentication.repository.entity.SignInLog;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseSignInLogRepository
    implements Repository<SignInLog> {

  private static final TableName SIGNIN_LOG_TABLE
      = TableName.valueOf("lms:signinlogs");

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

    Scan scan = HBaseScanFactory.createRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = hbaseClient.scan(scan, SIGNIN_LOG_TABLE);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    SignInLog signInLog = logMapper.from(results.get(0));
    return Optional.of(signInLog);
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