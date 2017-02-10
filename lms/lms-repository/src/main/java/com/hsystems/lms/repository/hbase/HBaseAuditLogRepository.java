package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.hbase.mapper.HBaseAuditLogMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseAuditLogRepository
    extends HBaseRepository implements AuditLogRepository {

  private final HBaseClient client;

  private final HBaseAuditLogMapper mapper;

  @Inject
  HBaseAuditLogRepository(
      HBaseClient client,
      HBaseAuditLogMapper mapper) {

    this.client = client;
    this.mapper = mapper;
  }

  @Override
  public Optional<AuditLog> findBy(String id, long timestamp) {
    return Optional.empty();
  }

  @Override
  public List<AuditLog> findAllBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    List<Result> results = client.scan(scan,
        Constants.TABLE_AUDIT_LOGS);

    if (results.isEmpty()) {
      return Collections.emptyList();
    }

    List<AuditLog> auditLogs = new ArrayList<>();
    results.stream().forEach(logResult -> {
      AuditLog auditLog = mapper.getEntity(Arrays.asList(logResult));
      auditLogs.add(auditLog);
    });
    return auditLogs;
  }

  @Override
  public void save(AuditLog auditLog, long timestamp)
      throws IOException {


  }

  @Override
  public void delete(AuditLog auditLog, long timestamp)
      throws IOException {


  }
}
