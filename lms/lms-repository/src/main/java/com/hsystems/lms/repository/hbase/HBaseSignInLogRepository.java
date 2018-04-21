package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.SignInLogRepository;
import com.hsystems.lms.repository.entity.SignInLog;
import com.hsystems.lms.repository.hbase.mapper.HBaseSignInLogMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseSignInLogRepository extends HBaseAbstractRepository
    implements SignInLogRepository {

  private final HBaseClient client;

  private final HBaseSignInLogMapper logMapper;

  @Inject
  HBaseSignInLogRepository(
      HBaseClient client,
      HBaseSignInLogMapper logMapper) {

    this.client = client;
    this.logMapper = logMapper;
  }

  @Override
  public Optional<SignInLog> findBy(String id)
      throws IOException {

    TableName tableName = getTableName(SignInLog.class);
    Get get = new Get(Bytes.toBytes(id));
    Result result = client.get(get, tableName);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    List<Result> results = Arrays.asList(result);
    return logMapper.getEntity(results);
  }

  @Override
  public void create(SignInLog entity)
      throws IOException {

  }

  @Override
  public void update(SignInLog entity)
      throws IOException {

  }

  @Override
  public void delete(SignInLog entity)
      throws IOException {

  }
}
