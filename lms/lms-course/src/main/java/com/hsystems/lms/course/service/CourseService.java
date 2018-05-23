package com.hsystems.lms.course.service;

import com.google.inject.Inject;

import com.hsystems.lms.course.repository.hbase.HBaseCourseRepository;
import com.hsystems.lms.course.repository.solr.SolrCourseRepository;

/**
 * Created by naungsoe on 15/10/16.
 */
public final class CourseService {

  private final HBaseCourseRepository hbaseCourseRepository;

  private final SolrCourseRepository solrCourseRepository;

  @Inject
  CourseService(
      HBaseCourseRepository hbaseCourseRepository,
      SolrCourseRepository solrCourseRepository) {

    this.hbaseCourseRepository = hbaseCourseRepository;
    this.solrCourseRepository = solrCourseRepository;
  }
}