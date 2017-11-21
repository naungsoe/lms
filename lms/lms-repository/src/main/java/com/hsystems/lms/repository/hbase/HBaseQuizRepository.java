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
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.entity.quiz.Quiz;
import com.hsystems.lms.repository.entity.quiz.QuizResource;
import com.hsystems.lms.repository.hbase.mapper.HBaseQuizMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseQuizRepository extends HBaseAbstractRepository
    implements QuizRepository {

  private final HBaseClient client;

  private final HBaseQuizMapper quizMapper;

  private final MutationRepository mutationRepository;

  private final ShareLogRepository shareLogRepository;

  private final AuditLogRepository auditLogRepository;

  @Inject
  HBaseQuizRepository(
      HBaseClient client,
      HBaseQuizMapper quizMapper,
      MutationRepository mutationRepository,
      ShareLogRepository shareLogRepository,
      AuditLogRepository auditLogRepository) {

    this.client = client;
    this.quizMapper = quizMapper;
    this.mutationRepository = mutationRepository;
    this.shareLogRepository = shareLogRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @Override
  public Optional<QuizResource> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.QUIZ);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    return findBy(id, mutation.getTimestamp());
  }

  private Optional<QuizResource> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, Quiz.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    Optional<QuizResource> resourceOptional
        = quizMapper.getEntity(results);

    if (resourceOptional.isPresent()) {
      QuizResource quizResource = resourceOptional.get();
      Optional<ShareLog> logOptional = shareLogRepository.findBy(id);

      if (logOptional.isPresent()) {
        ShareLog shareLog = logOptional.get();
        populatePermissionSets(quizResource, shareLog);
      }
    }

    return resourceOptional;
  }

  @Override
  public List<QuizResource> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        schoolId, lastId, limit, EntityType.QUIZ);

    if (CollectionUtils.isEmpty(mutations)) {
      return Collections.emptyList();
    }

    Mutation startMutation = mutations.get(0);
    Mutation stopMutation = mutations.get(mutations.size() - 1);
    String startRowKey = startMutation.getId();
    String stopRowKey = getInclusiveStopRowKey(stopMutation.getId());
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setStopRow(Bytes.toBytes(stopRowKey));
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = client.scan(scan, QuizResource.class);
    List<QuizResource> quizResources
        = quizMapper.getEntities(results, mutations);

    return quizResources;
  }

  @Override
  public void save(QuizResource entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    List<Put> puts = quizMapper.getPuts(entity, timestamp);
    client.put(puts, Quiz.class);

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(entity.getId(), EntityType.QUIZ);

    if (mutationOptional.isPresent()) {
      AuditLog auditLog = getAuditLog(entity, entity.getModifiedBy(),
          ActionType.MODIFIED, timestamp);
      auditLogRepository.save(auditLog);

      Mutation modifiedMutation = getMutation(entity,
          ActionType.MODIFIED, timestamp);
      mutationRepository.save(modifiedMutation);

      List<String> rowKeys = getPutRowKeys(puts);
      deleteUnusedRows(entity, rowKeys);

    } else {
      AuditLog auditLog = getAuditLog(entity, entity.getCreatedBy(),
          ActionType.CREATED, timestamp);
      auditLogRepository.save(auditLog);

      Mutation mutation = getMutation(entity,
          ActionType.CREATED, timestamp);
      mutationRepository.save(mutation);
    }
  }

  private void deleteUnusedRows(QuizResource entity, List<String> rowKeys)
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

  @Override
  public void delete(QuizResource entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    Mutation mutation = getMutation(entity, ActionType.DELETED, timestamp);
    mutationRepository.save(mutation);

    AuditLog auditLog = getAuditLog(entity,
        entity.getModifiedBy(), ActionType.DELETED, timestamp);
    auditLogRepository.save(auditLog);
  }
}