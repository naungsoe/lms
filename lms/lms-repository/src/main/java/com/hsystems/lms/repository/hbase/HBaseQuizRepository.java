package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.MutateLogRepository;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.MutateLog;
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

  private final HBaseQuizMapper quizMapper;

  private final MutateLogRepository mutateLogRepository;

  @Inject
  HBaseQuizRepository(
      HBaseClient client,
      HBaseQuizMapper quizMapper,
      MutateLogRepository mutateLogRepository) {

    this.client = client;
    this.quizMapper = quizMapper;
    this.mutateLogRepository = mutateLogRepository;
  }

  @Override
  public Optional<Quiz> findBy(String id)
      throws IOException {

    Optional<MutateLog> mutateLogOptional
        = mutateLogRepository.findBy(id, EntityType.QUIZ);

    if (!mutateLogOptional.isPresent()) {
      return Optional.empty();
    }

    MutateLog mutateLog = mutateLogOptional.get();
    Scan scan = getRowKeyFilterScan(id);
    scan.setTimeStamp(mutateLog.getTimestamp());

    List<Result> results = client.scan(scan, Quiz.class);

    if (results.isEmpty()) {
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
  public void delete(Quiz entity)
      throws IOException {

  }
}
