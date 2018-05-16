package com.hsystems.lms.quiz.repository;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.quiz.repository.entity.QuizResource;
import com.hsystems.lms.quiz.repository.hbase.HBaseQuizRepository;
import com.hsystems.lms.quiz.repository.solr.SolrQuizRepository;

import java.io.IOException;
import java.util.Optional;

public final class NoSqlQuizRepository implements QuizRepository {

  private final SolrQuizRepository solrQuizRepository;

  private final HBaseQuizRepository hbaseQuizRepository;

  @Inject
  public NoSqlQuizRepository(
      SolrQuizRepository solrQuizRepository,
      HBaseQuizRepository hbaseQuizRepository) {

    this.solrQuizRepository = solrQuizRepository;
    this.hbaseQuizRepository = hbaseQuizRepository;
  }

  @Override
  public QueryResult<Auditable<QuizResource>> findAllBy(Query query)
      throws IOException {

    return solrQuizRepository.findAllBy(query);
  }

  @Override
  public Optional<Auditable<QuizResource>> findBy(String id)
      throws IOException {

    return solrQuizRepository.findBy(id);
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