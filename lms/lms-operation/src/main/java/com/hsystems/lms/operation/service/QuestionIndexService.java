package com.hsystems.lms.operation.service;

import com.google.inject.Inject;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.question.repository.hbase.HBaseQuestionRepository;
import com.hsystems.lms.question.repository.solr.SolrQuestionRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class QuestionIndexService {

  private static final int INDEX_LIMIT = 50;

  private final HBaseQuestionRepository hbaseQuestionRepository;

  private final SolrQuestionRepository solrQuestionRepository;

  @Inject
  QuestionIndexService(
      HBaseQuestionRepository hbaseQuestionRepository,
      SolrQuestionRepository solrQuestionRepository) {

    this.hbaseQuestionRepository = hbaseQuestionRepository;
    this.solrQuestionRepository = solrQuestionRepository;
  }

  public void indexAll()
      throws IOException {

    String lastId = "";
    int numFound;

    do {
      List<Auditable<QuestionResource>> resources
          = hbaseQuestionRepository.findAllBy(lastId, INDEX_LIMIT);
      solrQuestionRepository.addAll(resources);

      numFound = resources.size();

      Auditable<QuestionResource> user = resources.get(numFound - 1);
      lastId = user.getEntity().getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  public void index(String id)
      throws IOException {

    Optional<Auditable<QuestionResource>> resourceOptional
        = hbaseQuestionRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      Auditable<QuestionResource> resource = resourceOptional.get();
      solrQuestionRepository.add(resource);
    }
  }
}