package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.FileRepository;
import com.hsystems.lms.repository.entity.file.FileResource;
import com.hsystems.lms.repository.hbase.mapper.HBaseFileMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by naungsoe on 12/10/16.
 */
public class HBaseFileRepository extends HBaseAbstractRepository
    implements FileRepository {

  private final HBaseClient client;

  private final HBaseFileMapper fileMapper;

  @Inject
  HBaseFileRepository(
      HBaseClient client,
      HBaseFileMapper fileMapper) {

    this.client = client;
    this.fileMapper = fileMapper;
  }

  @Override
  public List<FileResource> findAllBy(
      String schoolId, String lastId, int limit)
    throws IOException {

    String startRowKey = getExclusiveStartRowKey(lastId);
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    TableName tableName = getTableName(FileResource.class);
    List<Result> results = client.scan(scan, tableName);

    return fileMapper.getEntities(results);
  }

  @Override
  public List<FileResource> findAllBy(String schoolId, String parentId)
      throws IOException {

    Scan scan = getRowKeyFilterScan(schoolId, parentId);
    scan.setStartRow(Bytes.toBytes(schoolId));

    TableName tableName = getTableName(FileResource.class);
    List<Result> results = client.scan(scan, tableName);
    Set<String> rowKeys = new HashSet<>(results.size());
    results.forEach(result -> {
      String rowKey = fileMapper.getChildId(result);
      rowKeys.add(rowKey);
    });

    scan = getRowKeysFilterScan(rowKeys);
    scan.setStartRow(Bytes.toBytes(schoolId));
    scan.setMaxVersions(MAX_VERSIONS);

    results = client.scan(scan, tableName);
    return fileMapper.getEntities(results);
  }

  @Override
  public Optional<FileResource> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(FileResource.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return fileMapper.getEntity(results);
  }

  @Override
  public void create(FileResource entity)
      throws IOException {

  }

  @Override
  public void update(FileResource entity)
      throws IOException {

  }

  @Override
  public void delete(FileResource entity)
      throws IOException {

  }
}