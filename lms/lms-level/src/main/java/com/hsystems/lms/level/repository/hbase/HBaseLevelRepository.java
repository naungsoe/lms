package com.hsystems.lms.level.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;
import com.hsystems.lms.level.repository.entity.Level;

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
public final class HBaseLevelRepository
    implements Repository<Auditable<Level>> {

  private static final String LEVEL_TABLE = "lms:levels";

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseLevelMapper levelMapper;

  @Inject
  HBaseLevelRepository(HBaseClient hbaseClient) {
    this.hbaseClient = hbaseClient;
    this.levelMapper = new HBaseLevelMapper();
  }

  public List<Auditable<Level>> findAllBy(String lastId, int limit)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(lastId);
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    List<Result> results = hbaseClient.scan(scan, LEVEL_TABLE);
    List<Auditable<Level>> groups = new ArrayList<>();

    for (Result result : results) {
      groups.add(levelMapper.from(result));
    }

    return groups;
  }

  @Override
  public Optional<Auditable<Level>> findBy(String id)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));
    get.setMaxVersions(MAX_VERSIONS);

    Result result = hbaseClient.get(get, LEVEL_TABLE);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(levelMapper.from(result));
  }

  @Override
  public void add(Auditable<Level> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<Level> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<Level> entity)
      throws IOException {

  }
}