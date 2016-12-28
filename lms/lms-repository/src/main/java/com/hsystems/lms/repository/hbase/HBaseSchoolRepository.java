package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.entity.AuditLog;
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

  private final AuditLogRepository auditLogRepository;

  private final HBaseSchoolMapper mapper;

  @Inject
  HBaseSchoolRepository(
      HBaseClient client, AuditLogRepository auditLogRepository) {

    this.client = client;
    this.auditLogRepository = auditLogRepository;
    this.mapper = new HBaseSchoolMapper();
  }

  @Override
  public Optional<School> findBy(String id)
      throws IOException {

    Optional<AuditLog> auditLogOptional
        = auditLogRepository.findLastestLogBy(id);

    if (!auditLogOptional.isPresent()) {
      return Optional.empty();
    }

    Scan scan = getRowFilterScan(id);
    scan.setTimeStamp(auditLogOptional.get().getTimestamp());

    List<Result> results = client.scan(scan, Constants.TABLE_SCHOOLS);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    School school = mapper.map(results);
    return Optional.of(school);
  }
}
