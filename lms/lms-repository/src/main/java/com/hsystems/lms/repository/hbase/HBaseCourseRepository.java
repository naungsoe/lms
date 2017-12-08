package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.CourseRepository;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.entity.ActionType;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.course.CourseResource;
import com.hsystems.lms.repository.hbase.mapper.HBaseCourseMapper;
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
public class HBaseCourseRepository extends HBaseAbstractRepository
    implements CourseRepository {

  private final HBaseClient client;

  private final HBaseCourseMapper courseMapper;

  private final MutationRepository mutationRepository;

  private final AuditLogRepository auditLogRepository;

  @Inject
  HBaseCourseRepository(
      HBaseClient client,
      HBaseCourseMapper courseMapper,
      MutationRepository mutationRepository,
      AuditLogRepository auditLogRepository) {

    this.client = client;
    this.courseMapper = courseMapper;
    this.mutationRepository = mutationRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @Override
  public Optional<CourseResource> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.COURSE);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    return findBy(id, mutation.getTimestamp());
  }

  private Optional<CourseResource> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, CourseResource.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return courseMapper.getEntity(results);
  }

  @Override
  public List<CourseResource> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        schoolId, lastId, limit, EntityType.COURSE);

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

    List<Result> results = client.scan(scan, CourseResource.class);
    List<CourseResource> courseResources
        = courseMapper.getEntities(results, mutations);

    return courseResources;
  }

  @Override
  public void save(CourseResource entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    List<Put> puts = courseMapper.getPuts(entity, timestamp);
    client.put(puts, CourseResource.class);

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

  private void deleteUnusedRows(CourseResource entity, List<String> rowKeys)
      throws IOException {

    String startRowKey = entity.getId();
    Scan scan = getRowKeyOnlyFilterScan(startRowKey);
    scan.setStartRow(Bytes.toBytes(startRowKey));

    List<Result> results = client.scan(scan, CourseResource.class);
    List<String> origRowKeys = getResultRowKeys(results);
    List<String> unusedRowKeys = new ArrayList<>();
    origRowKeys.forEach(origRowKey -> {
      boolean usedRowKey = rowKeys.stream()
          .anyMatch(rowKey -> rowKey.equals(origRowKey));

      if (!usedRowKey) {
        unusedRowKeys.add(origRowKey);
      }
    });

    List<Delete> deletes = courseMapper.getDeletes(unusedRowKeys);
    client.delete(deletes, CourseResource.class);
  }

  @Override
  public void delete(CourseResource entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    Mutation mutation = getMutation(entity, ActionType.DELETED, timestamp);
    mutationRepository.save(mutation);

    AuditLog auditLog = getAuditLog(entity,
        entity.getModifiedBy(), ActionType.DELETED, timestamp);
    auditLogRepository.save(auditLog);
  }
}
