package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.hbase.mapper.HBaseUserMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 8/8/16.
 */
public class HBaseUserRepository extends HBaseAbstractRepository
    implements UserRepository {

  private final HBaseClient client;

  private final HBaseUserMapper userMapper;

  @Inject
  HBaseUserRepository(
      HBaseClient client,
      HBaseUserMapper userMapper) {

    this.client = client;
    this.userMapper = userMapper;
  }

  @Override
  public List<User> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    String startRowKey = getExclusiveStartRowKey(lastId);
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    TableName tableName = getTableName(User.class);
    List<Result> results = client.scan(scan, tableName);
    return userMapper.getEntities(results);
  }

  @Override
  public Optional<User> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(User.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return userMapper.getEntity(results);
  }

  @Override
  public void create(User entity)
      throws IOException {

  }

  @Override
  public void update(User entity)
      throws IOException {

  }

  @Override
  public void delete(User entity)
      throws IOException {

  }
}
