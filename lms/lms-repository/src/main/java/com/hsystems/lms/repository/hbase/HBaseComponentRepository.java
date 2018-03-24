package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.hbase.mapper.HBaseComponentMapper;
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
public class HBaseComponentRepository extends HBaseAbstractRepository
    implements ComponentRepository {

  private final HBaseClient client;

  private final HBaseComponentMapper componentMapper;

  @Inject
  HBaseComponentRepository(
      HBaseClient client,
      HBaseComponentMapper componentMapper) {

    this.client = client;
    this.componentMapper = componentMapper;
  }

  @Override
  public Optional<Component> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(Component.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return componentMapper.getEntity(results);
  }

  @Override
  public List<Component> findAllBy(String parentId)
      throws IOException {

    String startRowKey = getExclusiveStartRowKey(parentId);
    Scan scan = getRowKeyFilterScan(parentId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(Component.class);
    List<Result> results = client.scan(scan, tableName);
    List<Component> components = componentMapper.getEntities(results);

    return components;
  }

  @Override
  public void save(Component entity)
      throws IOException {

  }

  @Override
  public void save(Component entity, String parentId)
      throws IOException {

  }

  @Override
  public void delete(Component entity)
      throws IOException {

  }
}
