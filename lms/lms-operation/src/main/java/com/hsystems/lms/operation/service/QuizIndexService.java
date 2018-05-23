package com.hsystems.lms.operation.service;

import com.google.inject.Inject;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.quiz.repository.entity.QuizResource;
import com.hsystems.lms.quiz.repository.hbase.HBaseQuizRepository;
import com.hsystems.lms.quiz.repository.solr.SolrQuizRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class QuizIndexService {

  private static final int INDEX_LIMIT = 50;

  private final HBaseQuizRepository hbaseQuizRepository;

  private final SolrQuizRepository solrQuizRepository;

  @Inject
  QuizIndexService(
      HBaseQuizRepository hbaseQuizRepository,
      SolrQuizRepository solrQuizRepository) {

    this.hbaseQuizRepository = hbaseQuizRepository;
    this.solrQuizRepository = solrQuizRepository;
  }

  public void indexAll()
      throws IOException {

    String lastId = "";
    int numFound;

    do {
      List<Auditable<QuizResource>> resources
          = hbaseQuizRepository.findAllBy(lastId, INDEX_LIMIT);
      solrQuizRepository.addAll(resources);

      numFound = resources.size();

      Auditable<QuizResource> user = resources.get(numFound - 1);
      lastId = user.getEntity().getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  public void index(String id)
      throws IOException {

    Optional<Auditable<QuizResource>> resourceOptional
        = hbaseQuizRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      Auditable<QuizResource> resource = resourceOptional.get();
      solrQuizRepository.add(resource);
    }
  }
}