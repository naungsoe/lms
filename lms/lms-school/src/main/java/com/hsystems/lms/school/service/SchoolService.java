package com.hsystems.lms.school.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRepository;
import com.hsystems.lms.school.repository.solr.SolrSchoolRepository;
import com.hsystems.lms.school.service.mapper.SchoolModelMapper;
import com.hsystems.lms.school.service.model.SchoolModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public final class SchoolService {

  private final HBaseSchoolRepository hbaseSchoolRepository;

  private final SolrSchoolRepository solrSchoolRepository;

  private final SchoolModelMapper schoolMapper;

  @Inject
  SchoolService(
      HBaseSchoolRepository hbaseSchoolRepository,
      SolrSchoolRepository solrSchoolRepository) {

    this.hbaseSchoolRepository = hbaseSchoolRepository;
    this.solrSchoolRepository = solrSchoolRepository;
    this.schoolMapper = new SchoolModelMapper();
  }

  @Log
  @Requires(SchoolPermission.VIEW_SCHOOL)
  public QueryResult<SchoolModel> findAllBy(Query query)
      throws IOException {

    QueryResult<Auditable<School>> queryResult
        = solrSchoolRepository.findAllBy(query);
    long elapsedTime = queryResult.getElapsedTime();
    long start = queryResult.getStart();
    long numFound = queryResult.getNumFound();
    List<Auditable<School>> schools = queryResult.getItems();

    if (CollectionUtils.isEmpty(schools)) {
      return new QueryResult<>(elapsedTime, start,
          numFound, Collections.emptyList());
    }

    List<SchoolModel> schoolModels = new ArrayList<>();

    for (Auditable<School> school : schools) {
      SchoolModel schoolModel = schoolMapper.from(school);
      schoolModels.add(schoolModel);
    }

    return new QueryResult<>(elapsedTime, start,
        numFound, schoolModels);
  }

  @Log
  @Requires(SchoolPermission.VIEW_SCHOOL)
  public Optional<SchoolModel> findBy(String id)
      throws IOException {

    Optional<Auditable<School>> schoolOptional
        = hbaseSchoolRepository.findBy(id);

    if (schoolOptional.isPresent()) {
      Auditable<School> school = schoolOptional.get();
      SchoolModel schoolModel = schoolMapper.from(school);
      return Optional.of(schoolModel);
    }

    return Optional.empty();
  }
}