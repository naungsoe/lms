package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.entity.ActionType;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.entity.question.QuestionResource;
import com.hsystems.lms.repository.hbase.mapper.HBaseQuestionMapper;
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
public class HBaseQuestionRepository extends HBaseAbstractRepository
    implements QuestionRepository {

  private final HBaseClient client;

  private final HBaseQuestionMapper questionMapper;

  private final MutationRepository mutationRepository;

  private final ShareLogRepository shareLogRepository;

  private final AuditLogRepository auditLogRepository;

  @Inject
  HBaseQuestionRepository(
      HBaseClient client,
      HBaseQuestionMapper questionMapper,
      MutationRepository mutationRepository,
      ShareLogRepository shareLogRepository,
      AuditLogRepository auditLogRepository) {

    this.client = client;
    this.questionMapper = questionMapper;
    this.mutationRepository = mutationRepository;
    this.shareLogRepository = shareLogRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @Override
  public Optional<QuestionResource> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.QUESTION);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    return findBy(id, mutation.getTimestamp());
  }

  private Optional<QuestionResource> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, QuestionResource.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    Optional<QuestionResource> resourceOptional
        = questionMapper.getEntity(results);

    if (resourceOptional.isPresent()) {
      QuestionResource questionResource = resourceOptional.get();
      Optional<ShareLog> logOptional = shareLogRepository.findBy(id);

      if (logOptional.isPresent()) {
        ShareLog shareLog = logOptional.get();
        populateShareEntries(questionResource, shareLog);
      }
    }

    return resourceOptional;
  }

  @Override
  public List<QuestionResource> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        schoolId, lastId, limit, EntityType.QUESTION);

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

    List<Result> results = client.scan(scan, QuestionResource.class);
    List<QuestionResource> questionResources
        = questionMapper.getEntities(results, mutations);

    return questionResources;
  }

  @Override
  public void save(QuestionResource entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    List<Put> puts = questionMapper.getPuts(entity, timestamp);
    client.put(puts, QuestionResource.class);

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(entity.getId(), EntityType.QUESTION);

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

      Mutation mutation = getMutation(entity, ActionType.CREATED, timestamp);
      mutationRepository.save(mutation);
    }
  }

  private void deleteUnusedRows(
      QuestionResource entity, List<String> rowKeys)
      throws IOException {

    String startRowKey = entity.getId();
    Scan scan = getRowKeyOnlyFilterScan(startRowKey);
    scan.setStartRow(Bytes.toBytes(startRowKey));

    List<Result> results = client.scan(scan, QuestionResource.class);
    List<String> origRowKeys = getResultRowKeys(results);
    List<String> unusedRowKeys = new ArrayList<>();
    origRowKeys.forEach(origRowKey -> {
      boolean usedRowKey = rowKeys.stream()
          .anyMatch(rowKey -> rowKey.equals(origRowKey));

      if (!usedRowKey) {
        unusedRowKeys.add(origRowKey);
      }
    });

    List<Delete> deletes = questionMapper.getDeletes(unusedRowKeys);
    client.delete(deletes, QuestionResource.class);
  }

  @Override
  public void delete(QuestionResource entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    Mutation mutation = getMutation(entity, ActionType.DELETED, timestamp);
    mutationRepository.save(mutation);

    AuditLog auditLog = getAuditLog(entity,
        entity.getModifiedBy(), ActionType.DELETED, timestamp);
    auditLogRepository.save(auditLog);
  }
}