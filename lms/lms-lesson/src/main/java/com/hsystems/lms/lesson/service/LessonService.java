package com.hsystems.lms.lesson.service;

import com.google.inject.Inject;

import com.hsystems.lms.lesson.repository.hbase.HBaseLessonRepository;
import com.hsystems.lms.lesson.repository.solr.SolrLessonRepository;

/**
 * Created by naungsoe on 15/10/16.
 */
public final class LessonService {

  private final HBaseLessonRepository hbaseLessonRepository;

  private final SolrLessonRepository solrLessonRepository;

  @Inject
  LessonService(
      HBaseLessonRepository hbaseLessonRepository,
      SolrLessonRepository solrLessonRepository) {

    this.hbaseLessonRepository = hbaseLessonRepository;
    this.solrLessonRepository = solrLessonRepository;
  }
}