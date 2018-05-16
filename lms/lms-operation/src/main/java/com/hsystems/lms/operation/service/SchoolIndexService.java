package com.hsystems.lms.operation.service;

import com.google.inject.Inject;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRepository;
import com.hsystems.lms.school.repository.solr.SolrSchoolRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class SchoolIndexService {

  private static final int INDEX_LIMIT = 50;

  private final HBaseSchoolRepository hbaseSchoolRepository;

  private final SolrSchoolRepository solrSchoolRepository;

  @Inject
  SchoolIndexService(
      HBaseSchoolRepository hbaseSchoolRepository,
      SolrSchoolRepository solrSchoolRepository) {

    this.hbaseSchoolRepository = hbaseSchoolRepository;
    this.solrSchoolRepository = solrSchoolRepository;
  }

  public void indexAll()
      throws IOException {

    String lastId = "";
    int numFound;

    do {
      List<Auditable<School>> schools
          = hbaseSchoolRepository.findAllBy(lastId, INDEX_LIMIT);
      solrSchoolRepository.addAll(schools);

      numFound = schools.size();

      Auditable<School> school = schools.get(numFound - 1);
      lastId = school.getEntity().getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  public void index(String id)
      throws IOException {

    Optional<Auditable<School>> schoolOptional
        = hbaseSchoolRepository.findBy(id);

    if (schoolOptional.isPresent()) {
      Auditable<School> school = schoolOptional.get();
      solrSchoolRepository.add(school);
    }
  }
}