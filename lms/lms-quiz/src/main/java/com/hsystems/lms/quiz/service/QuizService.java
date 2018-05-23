package com.hsystems.lms.quiz.service;

import com.google.inject.Inject;

import com.hsystems.lms.quiz.repository.hbase.HBaseQuizRepository;
import com.hsystems.lms.quiz.repository.solr.SolrQuizRepository;

/**
 * Created by naungsoe on 15/10/16.
 */
public final class QuizService {

  private final HBaseQuizRepository hbaseQuizRepository;

  private final SolrQuizRepository solrQuizRepository;

  @Inject
  QuizService(
      HBaseQuizRepository hbaseQuizRepository,
      SolrQuizRepository solrQuizRepository) {

    this.hbaseQuizRepository = hbaseQuizRepository;
    this.solrQuizRepository = solrQuizRepository;
  }
}