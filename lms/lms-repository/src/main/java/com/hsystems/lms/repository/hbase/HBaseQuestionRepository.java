package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.ActionType;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.hbase.mapper.HBaseQuestionMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseQuestionRepository
    extends HBaseRepository implements QuestionRepository {

  private final HBaseClient client;

  private final HBaseQuestionMapper questionMapper;

  private final MutationRepository mutationRepository;

  private final AuditLogRepository auditLogRepository;

  @Inject
  HBaseQuestionRepository(
      HBaseClient client,
      HBaseQuestionMapper questionMapper,
      MutationRepository mutationRepository,
      AuditLogRepository auditLogRepository) {

    this.client = client;
    this.questionMapper = questionMapper;
    this.mutationRepository = mutationRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @Override
  public Optional<Question> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.QUESTION);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(mutation.getTimestamp());

    List<Result> results = client.scan(scan, Question.class);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    Question question = questionMapper.getEntity(results);
    return Optional.of(question);
  }

  @Override
  public List<Question> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        schoolId, lastId, limit, EntityType.QUESTION);

    if (mutations.isEmpty()) {
      return Collections.emptyList();
    }

    Mutation startMutation = mutations.get(0);
    Mutation stopMutation = mutations.get(mutations.size() - 1);
    String stopRowKey = getInclusiveStopRowKey(stopMutation.getId());
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startMutation.getId()));
    scan.setStopRow(Bytes.toBytes(stopRowKey));
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = client.scan(scan, Question.class);
    return questionMapper.getEntities(results, mutations);
  }

  @Override
  public void save(Question entity)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(entity.getId(), EntityType.QUESTION);
    ActionType actionType = mutationOptional.isPresent()
        ? ActionType.MODIFIED : ActionType.CREATED;

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    List<Put> puts = questionMapper.getPuts(entity, timestamp);
    client.put(puts, Question.class);

    Mutation mutation = getMutation(entity, actionType, timestamp);
    mutationRepository.save(mutation);

    User user = actionType.equals(ActionType.CREATED)
        ? entity.getModifiedBy() : entity.getCreatedBy();
    AuditLog auditLog = getAuditLog(entity, user, actionType, timestamp);
    auditLogRepository.save(auditLog);
  }

  @Override
  public void delete(Question entity)
      throws IOException {

    long timestamp = DateTimeUtils.getCurrentMilliseconds();
    Mutation mutation = getMutation(entity, ActionType.DELETED, timestamp);
    mutationRepository.save(mutation);

    AuditLog auditLog = getAuditLog(entity,
        entity.getModifiedBy(), ActionType.DELETED, timestamp);
    auditLogRepository.save(auditLog);
  }
}
