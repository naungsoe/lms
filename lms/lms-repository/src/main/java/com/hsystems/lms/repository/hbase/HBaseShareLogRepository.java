package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.hbase.mapper.HBaseShareLogMapper;
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
public class HBaseShareLogRepository extends HBaseAbstractRepository
    implements ShareLogRepository {

  private final HBaseClient client;

  private final HBaseShareLogMapper logMapper;

  @Inject
  HBaseShareLogRepository(
      HBaseClient client,
      HBaseShareLogMapper logMapper) {

    this.client = client;
    this.logMapper = logMapper;
  }

  @Override
  public Optional<ShareLog> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));

    TableName tableName = getTableName(ShareLog.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return logMapper.getEntity(results);
  }

  @Override
  public void save(ShareLog entity)
      throws IOException {

  }

  @Override
  public void delete(ShareLog entity)
      throws IOException {

  }
}
