package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.repository.hbase.mapper.HBaseQuestionMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseQuestionRepository
    extends HBaseRepository implements QuestionRepository {

  private final HBaseClient client;

  private final AuditLogRepository auditLogRepository;

  private final HBaseQuestionMapper mapper;

  @Inject
  HBaseQuestionRepository(
      HBaseClient client, AuditLogRepository auditLogRepository) {

    this.client = client;
    this.auditLogRepository = auditLogRepository;
    this.mapper = new HBaseQuestionMapper();
  }

  @Override
  public Optional<Question> findBy(String id)
      throws IOException {

    Optional<AuditLog> auditLogOptional
        = auditLogRepository.findLastestLogBy(id);

    if (!auditLogOptional.isPresent()) {
      return Optional.empty();
    }

    Scan scan = getRowFilterScan(id);
    scan.setTimeStamp(auditLogOptional.get().getTimestamp());

    List<Result> results = client.scan(scan, Constants.TABLE_QUESTIONS);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    Question question = mapper.map(results);
    return Optional.of(question);
  }
}
