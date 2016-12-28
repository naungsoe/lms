package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.hbase.mapper.HBaseAuditLogMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
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
public class HBaseAuditLogRepository
    extends HBaseRepository implements AuditLogRepository {

  private final HBaseClient client;

  private final AuditLogRepository auditLogRepository;

  private final HBaseAuditLogMapper mapper;

  @Inject
  HBaseAuditLogRepository(
      HBaseClient client, AuditLogRepository auditLogRepository) {

    this.client = client;
    this.auditLogRepository = auditLogRepository;
    this.mapper = new HBaseAuditLogMapper();
  }

  @Override
  public List<AuditLog> findAllBy(String id)
      throws IOException {

    Scan scan = getRowFilterScan(id);
    List<Result> results = client.scan(scan, Constants.TABLE_AUDIT_LOG);

    if (results.isEmpty()) {
      return Collections.emptyList();
    }

    List<AuditLog> auditLogs = new ArrayList<>();

    for (Result result : results) {
      auditLogs.add(mapper.map(Arrays.asList(result)));
    }

    return auditLogs;
  }

  @Override
  public Optional<AuditLog> findLastestLogBy(String id)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));
    Result result = client.get(get, Constants.TABLE_AUDIT_LOG);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    AuditLog auditLog = mapper.map(Arrays.asList(result));
    return Optional.of(auditLog);
  }

  @Override
  public void save(AuditLog auditLog)
      throws IOException {

    Put put = new Put(Bytes.toBytes(auditLog.getEntityId()));
    byte[] type = Bytes.toBytes(auditLog.getEntityType().toString());
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_TYPE, type);
    client.put(put, Constants.TABLE_AUDIT_LOG);
  }
}
