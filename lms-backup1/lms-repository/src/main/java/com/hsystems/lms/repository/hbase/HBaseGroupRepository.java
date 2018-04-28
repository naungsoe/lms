package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.hbase.mapper.HBaseGroupMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseGroupRepository extends HBaseAbstractRepository
    implements GroupRepository {

  private final HBaseClient client;

  private final HBaseGroupMapper groupMapper;

  @Inject
  HBaseGroupRepository(
      HBaseClient client,
      HBaseGroupMapper groupMapper) {

    this.client = client;
    this.groupMapper = groupMapper;
  }

  @Override
  public List<Group> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    String startRowKey = getExclusiveStartRowKey(lastId);
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    TableName tableName = getTableName(Group.class);
    List<Result> results = client.scan(scan, tableName);
    return groupMapper.getEntities(results);
  }

  @Override
  public Optional<Group> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(Group.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return groupMapper.getEntity(results);
  }

  @Override
  public void create(Group entity)
      throws IOException {

  }

  @Override
  public void update(Group entity)
      throws IOException {

  }

  @Override
  public void delete(Group entity)
      throws IOException {

  }
}
