package com.hsystems.lms.operation.service;

import com.google.inject.Inject;

import com.hsystems.lms.course.repository.entity.CourseResource;
import com.hsystems.lms.course.repository.hbase.HBaseCourseRepository;
import com.hsystems.lms.course.repository.solr.SolrCourseRepository;
import com.hsystems.lms.entity.Auditable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class CourseIndexService {

  private static final int INDEX_LIMIT = 50;

  private final HBaseCourseRepository hbaseCourseRepository;

  private final SolrCourseRepository solrCourseRepository;

  @Inject
  CourseIndexService(
      HBaseCourseRepository hbaseCourseRepository,
      SolrCourseRepository solrCourseRepository) {

    this.hbaseCourseRepository = hbaseCourseRepository;
    this.solrCourseRepository = solrCourseRepository;
  }

  public void indexAll()
      throws IOException {

    String lastId = "";
    int numFound;

    do {
      List<Auditable<CourseResource>> resources
          = hbaseCourseRepository.findAllBy(lastId, INDEX_LIMIT);
      solrCourseRepository.addAll(resources);

      numFound = resources.size();

      Auditable<CourseResource> user = resources.get(numFound - 1);
      lastId = user.getEntity().getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  public void index(String id)
      throws IOException {

    Optional<Auditable<CourseResource>> resourceOptional
        = hbaseCourseRepository.findBy(id);

    if (resourceOptional.isPresent()) {
      Auditable<CourseResource> resource = resourceOptional.get();
      solrCourseRepository.add(resource);
    }
  }
}