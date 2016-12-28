package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.Quiz;
import com.hsystems.lms.repository.hbase.mapper.HBaseQuizMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseQuizRepository
    extends HBaseRepository implements QuizRepository {

  private final HBaseClient client;

  private final AuditLogRepository auditLogRepository;

  private final HBaseQuizMapper mapper;

  @Inject
  HBaseQuizRepository(
      HBaseClient client, AuditLogRepository auditLogRepository) {

    this.client = client;
    this.auditLogRepository = auditLogRepository;
    this.mapper = new HBaseQuizMapper();
  }

  @Override
  public Optional<Quiz> findBy(String id)
      throws IOException {

    Optional<AuditLog> auditLogOptional
        = auditLogRepository.findLastestLogBy(id);

    if (!auditLogOptional.isPresent()) {
      return Optional.empty();
    }

    Scan scan = getRowFilterScan(id);
    scan.setTimeStamp(auditLogOptional.get().getTimestamp());

    List<Result> results = client.scan(scan, Constants.TABLE_QUIZZES);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    Quiz quiz = mapper.map(results);
    return Optional.of(quiz);
  }
}
