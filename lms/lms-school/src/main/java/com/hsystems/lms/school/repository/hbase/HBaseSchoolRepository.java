package com.hsystems.lms.school.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;
import com.hsystems.lms.school.repository.entity.School;

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
public final class HBaseSchoolRepository
    implements Repository<Auditable<School>> {

  private static final String SCHOOL_TABLE = "lms:schools";

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseSchoolMapper schoolMapper;

  @Inject
  HBaseSchoolRepository(HBaseClient hbaseClient) {
    this.hbaseClient = hbaseClient;
    this.schoolMapper = new HBaseSchoolMapper();
  }

  public List<Auditable<School>> findAllBy(String lastId, int limit)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(lastId);
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    List<Result> results = hbaseClient.scan(scan, SCHOOL_TABLE);
    List<Auditable<School>> schools = new ArrayList<>();

    for (Result result : results) {
      schools.add(schoolMapper.from(result));
    }

    return schools;
  }

  @Override
  public Optional<Auditable<School>> findBy(String id)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));
    get.setMaxVersions(MAX_VERSIONS);

    Result result = hbaseClient.get(get, SCHOOL_TABLE);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(schoolMapper.from(result));
  }

  @Override
  public void add(Auditable<School> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<School> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<School> entity)
      throws IOException {

  }
}