package com.hsystems.lms.group.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.group.repository.entity.Group;
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
public final class HBaseGroupRepository
    implements Repository<Auditable<Group>> {

  private static final TableName GROUP_TABLE
      = TableName.valueOf("lms:groups");

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseGroupMapper groupMapper;

  @Inject
  HBaseGroupRepository(HBaseClient hbaseClient) {
    this.hbaseClient = hbaseClient;
    this.groupMapper = new HBaseGroupMapper();
  }

  public List<Auditable<Group>> findAllBy(String lastId, int limit)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(lastId);
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    List<Result> results = hbaseClient.scan(scan, GROUP_TABLE);
    List<Auditable<Group>> groups = new ArrayList<>();
    HBaseUtils.forEachRowSetResults(results, rowSetResults -> {
      Auditable<Group> group = groupMapper.from(rowSetResults);
      groups.add(group);
    });

    return groups;
  }

  @Override
  public Optional<Auditable<Group>> findBy(String id)
      throws IOException {

    Scan scan = HBaseScanFactory.createRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = hbaseClient.scan(scan, GROUP_TABLE);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    Auditable<Group> group = groupMapper.from(results);
    return Optional.of(group);
  }

  @Override
  public void add(Auditable<Group> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<Group> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<Group> entity)
      throws IOException {

  }
}