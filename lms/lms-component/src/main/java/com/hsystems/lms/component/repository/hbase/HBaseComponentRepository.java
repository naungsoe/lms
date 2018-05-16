package com.hsystems.lms.component.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;
import com.hsystems.lms.hbase.HBaseUtils;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseComponentRepository
    implements Repository<Nested<Component>> {

  private static final TableName COMPONENT_TABLE
      = TableName.valueOf("lms:components");

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private HBaseComponentMapper componentMapper;

  @Inject
  HBaseComponentRepository(HBaseClient hbaseClient) {
    this.hbaseClient = hbaseClient;
  }

  public void setComponentMapper(HBaseComponentMapper componentMapper) {
    this.componentMapper = componentMapper;
  }

  public List<Nested<Component>> findAllBy(String resourceId)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(resourceId);
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = hbaseClient.scan(scan, COMPONENT_TABLE);
    List<Nested<Component>> components = new ArrayList<>();
    HBaseUtils.forEachRowSetResults(results, rowSetResults -> {
      Nested<Component> component = componentMapper.from(results);
      components.add(component);
    });

    return components;
  }

  @Override
  public Optional<Nested<Component>> findBy(String id)
      throws IOException {

    Scan scan = HBaseScanFactory.createRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = hbaseClient.scan(scan, COMPONENT_TABLE);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    scan = HBaseScanFactory.createRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    Nested<Component> component = componentMapper.from(results);
    return Optional.of(component);
  }

  @Override
  public void add(Nested<Component> entity)
      throws IOException {

  }

  @Override
  public void update(Nested<Component> entity)
      throws IOException {

  }

  @Override
  public void remove(Nested<Component> entity)
      throws IOException {

  }
}