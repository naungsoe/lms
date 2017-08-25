package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.entity.ActionType;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.quiz.Quiz;
import com.hsystems.lms.repository.hbase.mapper.HBaseQuizMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseQuizRepository extends HBaseResourceRepository
    implements QuizRepository {

  private final HBaseClient client;

  private final HBaseQuizMapper quizMapper;

  private final MutationRepository mutationRepository;

  private final AuditLogRepository auditLogRepository;

  @Inject
  HBaseQuizRepository(
      HBaseClient client,
      HBaseQuizMapper quizMapper,
      MutationRepository mutationRepository,
      AuditLogRepository auditLogRepository,
      ShareLogRepository shareLogRepository) {

    this.client = client;
    this.quizMapper = quizMapper;
    this.mutationRepository = mutationRepository;
    this.auditLogRepository = auditLogRepository;
    this.shareLogRepository = shareLogRepository;
  }

  @Override
  public Optional<Quiz> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.QUIZ);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    return findBy(id, mutation.getTimestamp());
  }

  private Optional<Quiz> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, Quiz.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    Quiz quiz = quizMapper.getEntity(results);
    populatePermissions(quiz);
    return Optional.of(quiz);
  }

  @Override
  public void save(Quiz entity)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(entity.getId(), EntityType.QUESTION);

    if (mutationOptional.isPresent()) {
      saveQuiz(entity);

    } else {
      createQuiz(entity);
    }
  }

  private void saveQuiz(Quiz entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    List<Put> puts = quizMapper.getPuts(entity, timestamp);
    client.put(puts, Quiz.class);

    AuditLog auditLog = getAuditLog(entity, entity.getModifiedBy(),
        ActionType.MODIFIED, timestamp);
    auditLogRepository.save(auditLog);

    Mutation modifiedMutation = getMutation(entity,
        ActionType.MODIFIED, timestamp);
    mutationRepository.save(modifiedMutation);

    List<String> rowKeys = getPutRowKeys(puts);
    deleteUnusedRows(entity, rowKeys);
  }

  private void deleteUnusedRows(Quiz entity, List<String> rowKeys)
      throws IOException {

    String startRowKey = entity.getId();
    Scan scan = getRowKeyOnlyFilterScan(startRowKey);
    scan.setStartRow(Bytes.toBytes(startRowKey));

    List<Result> results = client.scan(scan, Quiz.class);
    List<String> origRowKeys = getResultRowKeys(results);
    List<String> unusedRowKeys = new ArrayList<>();
    origRowKeys.forEach(origRowKey -> {
      boolean usedRowKey = rowKeys.stream()
          .anyMatch(rowKey -> rowKey.equals(origRowKey));

      if (!usedRowKey) {
        unusedRowKeys.add(origRowKey);
      }
    });

    List<Delete> deletes = quizMapper.getDeletes(unusedRowKeys);
    client.delete(deletes, Quiz.class);
  }

  private void createQuiz(Quiz entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    List<Put> puts = quizMapper.getPuts(entity, timestamp);
    client.put(puts, Quiz.class);

    AuditLog auditLog = getAuditLog(entity, entity.getCreatedBy(),
        ActionType.CREATED, timestamp);
    auditLogRepository.save(auditLog);

    Mutation mutation = getMutation(entity, ActionType.CREATED, timestamp);
    mutationRepository.save(mutation);
  }

  @Override
  public void delete(Quiz entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    Mutation mutation = getMutation(entity, ActionType.DELETED, timestamp);
    mutationRepository.save(mutation);

    AuditLog auditLog = getAuditLog(entity,
        entity.getModifiedBy(), ActionType.DELETED, timestamp);
    auditLogRepository.save(auditLog);
  }
}