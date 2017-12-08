package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.LessonRepository;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.entity.ActionType;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.lesson.LessonResource;
import com.hsystems.lms.repository.hbase.mapper.HBaseLessonMapper;
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
public class HBaseLessonRepository extends HBaseAbstractRepository
    implements LessonRepository {

  private final HBaseClient client;

  private final HBaseLessonMapper lessonMapper;

  private final MutationRepository mutationRepository;

  private final AuditLogRepository auditLogRepository;

  @Inject
  HBaseLessonRepository(
      HBaseClient client,
      HBaseLessonMapper lessonMapper,
      MutationRepository mutationRepository,
      AuditLogRepository auditLogRepository) {

    this.client = client;
    this.lessonMapper = lessonMapper;
    this.mutationRepository = mutationRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @Override
  public Optional<LessonResource> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.LESSON);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    return findBy(id, mutation.getTimestamp());
  }

  private Optional<LessonResource> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, LessonResource.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return lessonMapper.getEntity(results);
  }

  @Override
  public List<LessonResource> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        schoolId, lastId, limit, EntityType.LESSON);

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

    List<Result> results = client.scan(scan, LessonResource.class);
    List<LessonResource> lessonResources
        = lessonMapper.getEntities(results, mutations);

    return lessonResources;
  }

  @Override
  public void save(LessonResource entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    List<Put> puts = lessonMapper.getPuts(entity, timestamp);
    client.put(puts, LessonResource.class);

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

      Mutation mutation = getMutation(entity,
          ActionType.CREATED, timestamp);
      mutationRepository.save(mutation);
    }
  }

  private void deleteUnusedRows(LessonResource entity, List<String> rowKeys)
      throws IOException {

    String startRowKey = entity.getId();
    Scan scan = getRowKeyOnlyFilterScan(startRowKey);
    scan.setStartRow(Bytes.toBytes(startRowKey));

    List<Result> results = client.scan(scan, LessonResource.class);
    List<String> origRowKeys = getResultRowKeys(results);
    List<String> unusedRowKeys = new ArrayList<>();
    origRowKeys.forEach(origRowKey -> {
      boolean usedRowKey = rowKeys.stream()
          .anyMatch(rowKey -> rowKey.equals(origRowKey));

      if (!usedRowKey) {
        unusedRowKeys.add(origRowKey);
      }
    });

    List<Delete> deletes = lessonMapper.getDeletes(unusedRowKeys);
    client.delete(deletes, LessonResource.class);
  }

  @Override
  public void delete(LessonResource entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    Mutation mutation = getMutation(entity, ActionType.DELETED, timestamp);
    mutationRepository.save(mutation);

    AuditLog auditLog = getAuditLog(entity,
        entity.getModifiedBy(), ActionType.DELETED, timestamp);
    auditLogRepository.save(auditLog);
  }
}
