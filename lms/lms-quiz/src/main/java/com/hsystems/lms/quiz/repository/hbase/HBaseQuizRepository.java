package com.hsystems.lms.quiz.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.component.repository.hbase.HBaseComponentRepository;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.quiz.repository.entity.QuizResource;

import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseQuizRepository
    implements Repository<Auditable<QuizResource>> {

  private static final TableName QUIZ_TABLE
      = TableName.valueOf("lms:quizzes");

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseComponentRepository componentRepository;

  @Inject
  HBaseQuizRepository(
      HBaseClient hbaseClient,
      HBaseComponentRepository componentRepository) {

    this.hbaseClient = hbaseClient;
    this.componentRepository = componentRepository;
  }

  public List<Auditable<QuizResource>> findAllBy(String lastId, int limit)
      throws IOException {

    return Collections.emptyList();
  }

  @Override
  public Optional<Auditable<QuizResource>> findBy(String id)
      throws IOException {

    return Optional.empty();
  }

  @Override
  public void add(Auditable<QuizResource> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<QuizResource> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<QuizResource> entity)
      throws IOException {

  }
}