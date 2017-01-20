package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.QuizRepository;
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

  private final HBaseQuizMapper mapper;

  @Inject
  HBaseQuizRepository(
      HBaseClient client,
      HBaseQuizMapper mapper) {

    this.client = client;
    this.mapper = mapper;
  }

  @Override
  public Optional<Quiz> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan,
        Constants.TABLE_QUIZZES);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    Quiz quiz = mapper.getEntity(results);
    return Optional.of(quiz);
  }

  @Override
  public void save(Quiz entity, long timestamp)
      throws IOException {

  }

  @Override
  public void delete(Quiz entity, long timestamp)
      throws IOException {

  }
}
