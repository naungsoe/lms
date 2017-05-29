package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.QuestionComponent;
import com.hsystems.lms.repository.entity.Quiz;
import com.hsystems.lms.repository.hbase.mapper.HBaseQuizMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseQuizRepository
    extends HBaseRepository implements QuizRepository {

  private final HBaseClient client;

  private final HBaseQuizMapper quizMapper;

  private final MutationRepository mutationRepository;

  private final AuditLogRepository auditLogRepository;

  @Inject
  HBaseQuizRepository(
      HBaseClient client,
      HBaseQuizMapper quizMapper,
      MutationRepository mutationRepository,
      AuditLogRepository auditLogRepository) {

    this.client = client;
    this.quizMapper = quizMapper;
    this.mutationRepository = mutationRepository;
    this.auditLogRepository = auditLogRepository;
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
    return Optional.of(quiz);
  }

  @Override
  public void save(Quiz entity)
      throws IOException {

  }

  @Override
  public void save(QuestionComponent entity)
      throws IOException {

  }

  @Override
  public void delete(Quiz entity)
      throws IOException {

  }
}