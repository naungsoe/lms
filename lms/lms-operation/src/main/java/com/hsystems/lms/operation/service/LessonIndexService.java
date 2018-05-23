package com.hsystems.lms.operation.service;

import com.google.inject.Inject;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.lesson.repository.entity.LessonResource;
import com.hsystems.lms.lesson.repository.hbase.HBaseLessonRepository;
import com.hsystems.lms.lesson.repository.solr.SolrLessonRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class LessonIndexService {

  private static final int INDEX_LIMIT = 50;

  private final HBaseLessonRepository hbaseLessonRepository;

  private final SolrLessonRepository solrLessonRepository;

  @Inject
  LessonIndexService(
      HBaseLessonRepository hbaseLessonRepository,
      SolrLessonRepository solrLessonRepository) {

    this.hbaseLessonRepository = hbaseLessonRepository;
    this.solrLessonRepository = solrLessonRepository;
  }

  public void indexAll()
      throws IOException {

    String lastId = "";
    int numFound;

    do {
      List<Auditable<LessonResource>> resources
          = hbaseLessonRepository.findAllBy(lastId, INDEX_LIMIT);
      solrLessonRepository.addAll(resources);

      numFound = resources.size();

      Auditable<LessonResource> user = resources.get(numFound - 1);
      lastId = user.getEntity().getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  public void index(String id)
      throws IOException {

    Optional<Auditable<LessonResource>> resourceOptional
        = hbaseLessonRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      Auditable<LessonResource> resource = resourceOptional.get();
      solrLessonRepository.add(resource);
    }
  }
}