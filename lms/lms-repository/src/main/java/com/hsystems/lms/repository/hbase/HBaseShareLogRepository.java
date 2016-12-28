package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.hbase.mapper.HBaseShareLogMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseShareLogRepository
    extends HBaseRepository implements ShareLogRepository {

  private final HBaseClient client;

  private final AuditLogRepository auditLogRepository;

  private final HBaseShareLogMapper mapper;

  @Inject
  HBaseShareLogRepository(
      HBaseClient client, AuditLogRepository auditLogRepository) {

    this.client = client;
    this.auditLogRepository = auditLogRepository;
    this.mapper = new HBaseShareLogMapper();
  }

  @Override
  public Optional<ShareLog> findBy(String id)
      throws IOException {

    Optional<AuditLog> auditLogOptional
        = auditLogRepository.findLastestLogBy(id);

    if (!auditLogOptional.isPresent()) {
      return Optional.empty();
    }

    Scan scan = getRowFilterScan(id);
    scan.setTimeStamp(auditLogOptional.get().getTimestamp());

    List<Result> results = client.scan(scan, Constants.TABLE_SHARE_LOG);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    ShareLog shareLog = mapper.map(results);
    return Optional.of(shareLog);
  }
}
