package com.hsystems.lms.subject.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;
import com.hsystems.lms.subject.repository.entity.Subject;

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
public final class HBaseSubjectRepository
    implements Repository<Auditable<Subject>> {

  private static final String GROUP_TABLE = "lms:groups";

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseSubjectMapper subjectMapper;

  @Inject
  HBaseSubjectRepository(HBaseClient hbaseClient) {
    this.hbaseClient = hbaseClient;
    this.subjectMapper = new HBaseSubjectMapper();
  }

  public List<Auditable<Subject>> findAllBy(String lastId, int limit)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(lastId);
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    List<Result> results = hbaseClient.scan(scan, GROUP_TABLE);
    List<Auditable<Subject>> subjects = new ArrayList<>();

    for (Result result : results) {
      subjects.add(subjectMapper.from(result));
    }

    return subjects;
  }

  @Override
  public Optional<Auditable<Subject>> findBy(String id)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));
    get.setMaxVersions(MAX_VERSIONS);

    Result result = hbaseClient.get(get, GROUP_TABLE);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(subjectMapper.from(result));
  }

  @Override
  public void add(Auditable<Subject> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<Subject> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<Subject> entity)
      throws IOException {

  }
}