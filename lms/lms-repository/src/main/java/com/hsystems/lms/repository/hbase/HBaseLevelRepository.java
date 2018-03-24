package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.LevelRepository;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.hbase.mapper.HBaseLevelMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

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
public class HBaseLevelRepository extends HBaseAbstractRepository
    implements LevelRepository {

  private final HBaseClient client;

  private final HBaseLevelMapper levelMapper;

  @Inject
  HBaseLevelRepository(
      HBaseClient client,
      HBaseLevelMapper levelMapper) {

    this.client = client;
    this.levelMapper = levelMapper;
  }

  @Override
  public Optional<Level> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(Level.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return levelMapper.getEntity(results);
  }

  @Override
  public List<Level> findAllBy(String schoolId)
    throws IOException {

    String startRowKey = getExclusiveStartRowKey(schoolId);
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(Level.class);
    List<Result> results = client.scan(scan, tableName);
    return levelMapper.getEntities(results);
  }

  @Override
  public void save(Level entity)
      throws IOException {

  }

  @Override
  public void delete(Level entity)
      throws IOException {

  }
}
