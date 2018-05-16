package com.hsystems.lms.question.repository;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.question.repository.hbase.HBaseQuestionRepository;
import com.hsystems.lms.question.repository.solr.SolrQuestionRepository;

import java.io.IOException;
import java.util.Optional;

public final class NoSqlQuestionRepository implements QuestionRepository {

  private final SolrQuestionRepository solrQuestionRepository;

  private final HBaseQuestionRepository hbaseQuestionRepository;

  @Inject
  public NoSqlQuestionRepository(
      SolrQuestionRepository solrQuestionRepository,
      HBaseQuestionRepository hbaseQuestionRepository) {

    this.solrQuestionRepository = solrQuestionRepository;
    this.hbaseQuestionRepository = hbaseQuestionRepository;
  }

  @Override
  public QueryResult<Auditable<QuestionResource>> findAllBy(Query query)
      throws IOException {

    return solrQuestionRepository.findAllBy(query);
  }

  @Override
  public Optional<Auditable<QuestionResource>> findBy(String id)
      throws IOException {

    return solrQuestionRepository.findBy(id);
  }

  @Override
  public void add(Auditable<QuestionResource> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<QuestionResource> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<QuestionResource> entity)
      throws IOException {

  }
}