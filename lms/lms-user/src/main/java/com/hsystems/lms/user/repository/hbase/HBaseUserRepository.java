package com.hsystems.lms.user.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.user.repository.entity.AppUser;

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
public final class HBaseUserRepository
    implements Repository<Auditable<AppUser>> {

  private static final TableName USER_TABLE
      = TableName.valueOf("lms:users");

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseUserMapper userMapper;

  @Inject
  HBaseUserRepository(HBaseClient hbaseClient) {
    this.hbaseClient = hbaseClient;
    this.userMapper = new HBaseUserMapper();
  }

  public List<Auditable<AppUser>> findAllBy(String lastId, int limit)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(lastId);
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    List<Result> results = hbaseClient.scan(scan, USER_TABLE);
    List<Auditable<AppUser>> users = new ArrayList<>();
    HBaseUtils.forEachRowSetResults(results, rowSetResults -> {
      Auditable<AppUser> user = userMapper.from(rowSetResults);
      users.add(user);
    });

    return users;
  }

  @Override
  public Optional<Auditable<AppUser>> findBy(String id)
      throws IOException {

    Scan scan = HBaseScanFactory.createRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = hbaseClient.scan(scan, USER_TABLE);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    Auditable<AppUser> user = userMapper.from(results);
    return Optional.of(user);
  }

  @Override
  public void add(Auditable<AppUser> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<AppUser> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<AppUser> entity)
      throws IOException {

  }
}