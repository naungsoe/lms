package com.hsystems.lms.component.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;

import org.apache.hadoop.hbase.client.Get;
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

  private static final String COMPONENT_TABLE = "lms:components";

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private HBaseComponentMapperFactory mapperFactory;

  @Inject
  HBaseComponentRepository(HBaseClient hbaseClient) {
    this.hbaseClient = hbaseClient;
  }

  public void setMapperFactory(HBaseComponentMapperFactory mapperFactory) {
    this.mapperFactory = mapperFactory;
  }

  public List<Nested<Component>> findAllBy(String resourceId)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(resourceId);
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = hbaseClient.scan(scan, COMPONENT_TABLE);
    List<Nested<Component>> components = new ArrayList<>();

    for (Result result : results) {
      HBaseComponentMapper<Component> componentMapper
          = mapperFactory.create(result);
      components.add(componentMapper.from(result));
    }

    return components;
  }

  @Override
  public Optional<Nested<Component>> findBy(String id)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));
    get.setMaxVersions(MAX_VERSIONS);

    Result result = hbaseClient.get(get, COMPONENT_TABLE);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    HBaseComponentMapper<Component> componentMapper
        = mapperFactory.create(result);
    return Optional.of(componentMapper.from(result));
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