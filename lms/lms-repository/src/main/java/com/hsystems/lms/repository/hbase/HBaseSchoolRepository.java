package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.MutateLogRepository;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.MutateLog;
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

  private final HBaseSchoolMapper schoolMapper;

  private final MutateLogRepository mutateLogRepository;

  @Inject
  HBaseSchoolRepository(
      HBaseClient client,
      HBaseSchoolMapper schoolMapper,
      MutateLogRepository mutateLogRepository) {

    this.client = client;
    this.schoolMapper = schoolMapper;
    this.mutateLogRepository = mutateLogRepository;
  }

  @Override
  public Optional<School> findBy(String id)
      throws IOException {

    Optional<MutateLog> mutateLogOptional
        = mutateLogRepository.findBy(id, EntityType.SCHOOL);

    if (!mutateLogOptional.isPresent()) {
      return Optional.empty();
    }

    MutateLog mutateLog = mutateLogOptional.get();
    Scan scan = getRowKeyFilterScan(id);
    scan.setTimeStamp(mutateLog.getTimestamp());

    List<Result> results = client.scan(scan, School.class);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    School school = schoolMapper.getEntity(results);
    return Optional.of(school);
  }

  @Override
  public void save(School entity)
      throws IOException {

  }

  @Override
  public void delete(School entity)
      throws IOException {

  }
}
