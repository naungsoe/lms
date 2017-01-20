package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.hbase.mapper.HBaseSchoolMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public class HBaseSchoolRepository
    extends HBaseRepository implements SchoolRepository {

  private final HBaseClient client;

  private final HBaseSchoolMapper mapper;

  @Inject
  HBaseSchoolRepository(
      HBaseClient client,
      HBaseSchoolMapper mapper) {

    this.client = client;
    this.mapper = mapper;
  }

  @Override
  public Optional<School> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, Constants.TABLE_SCHOOLS);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    School school = mapper.getEntity(results);
    return Optional.of(school);
  }

  @Override
  public void save(School entity, long timestamp) throws IOException {

  }

  @Override
  public void delete(School entity, long timestamp) throws IOException {

  }
}
