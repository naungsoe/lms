package com.hsystems.lms.school.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.SchoolRepository;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.mapper.SchoolModelMapper;
import com.hsystems.lms.school.service.model.SchoolModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 15/10/16.
 */
public final class SchoolService {

  private final Provider<Properties> propertiesProvider;

  private final SchoolRepository schoolRepository;

  private final SchoolModelMapper schoolMapper;

  @Inject
  SchoolService(
      Provider<Properties> propertiesProvider,
      SchoolRepository schoolRepository) {

    this.propertiesProvider = propertiesProvider;
    this.schoolRepository = schoolRepository;
    this.schoolMapper = new SchoolModelMapper();
  }

  @Log
  @Requires(SchoolPermission.VIEW_SCHOOL)
  public QueryResult<SchoolModel> findAllBy(Query query)
      throws IOException {

    QueryResult<Auditable<School>> queryResult
        = schoolRepository.findAllBy(query);
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
        = schoolRepository.findBy(id);

    if (schoolOptional.isPresent()) {
      Auditable<School> school = schoolOptional.get();
      SchoolModel schoolModel = schoolMapper.from(school);
      return Optional.of(schoolModel);
    }

    return Optional.empty();
  }
}