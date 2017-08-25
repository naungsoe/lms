package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.hbase.mapper.HBaseAuditLogMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseAuditLogRepository extends HBaseRepository
    implements AuditLogRepository {

  private final HBaseClient client;

  private final HBaseAuditLogMapper auditLogMapper;

  @Inject
  HBaseAuditLogRepository(
      HBaseClient client,
      HBaseAuditLogMapper auditLogMapper) {

    this.client = client;
    this.auditLogMapper = auditLogMapper;
  }

  @Override
  public Optional<AuditLog> findBy(String id) {
    return Optional.empty();
  }

  @Override
  public List<AuditLog> findAllBy(String entityId)
      throws IOException {

    Scan scan = getRowKeyFilterScan(entityId);
    scan.setStartRow(Bytes.toBytes(entityId));
    List<Result> results = client.scan(scan, AuditLog.class);

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<AuditLog> auditLogs = new ArrayList<>();
    results.forEach(result -> {
      AuditLog auditLog = auditLogMapper.getEntity(Arrays.asList(result));
      auditLogs.add(auditLog);
    });

    return auditLogs;
  }

  @Override
  public void save(AuditLog auditLog)
      throws IOException {

  }

  @Override
  public void delete(AuditLog auditLog)
      throws IOException {

  }
}
