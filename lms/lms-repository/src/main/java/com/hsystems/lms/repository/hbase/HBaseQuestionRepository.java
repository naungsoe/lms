package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.ActionType;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.MutateLogRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.MutateLog;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.hbase.mapper.HBaseQuestionMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Put;
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

  private final HBaseQuestionMapper questionMapper;

  private final MutateLogRepository mutateLogRepository;

  private final AuditLogRepository auditLogRepository;

  @Inject
  HBaseQuestionRepository(
      HBaseClient client,
      HBaseQuestionMapper questionMapper,
      MutateLogRepository mutateLogRepository,
      AuditLogRepository auditLogRepository) {

    this.client = client;
    this.questionMapper = questionMapper;
    this.mutateLogRepository = mutateLogRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @Override
  public Optional<Question> findBy(String id)
      throws IOException {

    Optional<MutateLog> mutateLogOptional
        = mutateLogRepository.findBy(id, EntityType.QUESTION);

    if (!mutateLogOptional.isPresent()) {
      return Optional.empty();
    }

    MutateLog mutateLog = mutateLogOptional.get();
    Scan scan = getRowKeyFilterScan(id);
    scan.setTimeStamp(mutateLog.getTimestamp());

    List<Result> results = client.scan(scan, Question.class);

    if (results.isEmpty()) {
      return Optional.empty();
    }


    Question question = questionMapper.getEntity(results);
    return Optional.of(question);
  }

  @Override
  public void save(Question entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    List<Put> puts = questionMapper.getPuts(entity, timestamp);
    client.put(puts, Question.class);

    Optional<MutateLog> mutateLogOptional
        = mutateLogRepository.findBy(entity.getId());
    ActionType actionType = mutateLogOptional.isPresent()
        ? ActionType.MODIFIED : ActionType.CREATED;
    MutateLog mutateLog = getMutateLog(entity, actionType, timestamp);
    mutateLogRepository.save(mutateLog);

    User user = actionType.equals(ActionType.CREATED)
        ? entity.getModifiedBy() : entity.getCreatedBy();
    AuditLog auditLog = getAuditLog(entity, user, actionType, timestamp);
    auditLogRepository.save(auditLog);
  }

  @Override
  public void delete(Question entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    MutateLog mutateLog = getMutateLog(entity, ActionType.DELETED, timestamp);
    mutateLogRepository.save(mutateLog);

    AuditLog auditLog = getAuditLog(entity,
        entity.getModifiedBy(), ActionType.DELETED, timestamp);
    auditLogRepository.save(auditLog);
  }
}
