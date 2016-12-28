package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.hbase.mapper.HBaseUserMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 8/8/16.
 */
public class HBaseUserRepository
    extends HBaseRepository implements UserRepository {

  private final HBaseClient client;

  private final AuditLogRepository auditLogRepository;

  private final HBaseUserMapper mapper;

  @Inject
  HBaseUserRepository(
      HBaseClient client, AuditLogRepository auditLogRepository) {

    this.client = client;
    this.auditLogRepository = auditLogRepository;
    this.mapper = new HBaseUserMapper();
  }

  @Override
  public Optional<User> findBy(String id)
      throws IOException {

    Optional<AuditLog> auditLogOptional
        = auditLogRepository.findLastestLogBy(id);

    if (!auditLogOptional.isPresent()) {
      return Optional.empty();
    }

    Scan scan = getRowFilterScan(id);
    scan.setTimeStamp(auditLogOptional.get().getTimestamp());

    List<Result> results = client.scan(scan, Constants.TABLE_USERS);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    User user = mapper.map(results);
    return Optional.of(user);
  }

  @Override
  public void save(User user) {

  }
}
