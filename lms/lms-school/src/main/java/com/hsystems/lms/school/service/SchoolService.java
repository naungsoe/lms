package com.hsystems.lms.school.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.SchoolRepository;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.mapper.SchoolModelMapper;
import com.hsystems.lms.school.service.model.SchoolModel;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 15/10/16.
 */
public final class SchoolService {

  private final Provider<Properties> propertiesProvider;

  private final SchoolRepository schoolRepository;

  private final SchoolModelMapper schoolModelMapper;

  @Inject
  SchoolService(
      Provider<Properties> propertiesProvider,
      SchoolRepository schoolRepository) {

    this.propertiesProvider = propertiesProvider;
    this.schoolRepository = schoolRepository;
    this.schoolModelMapper = new SchoolModelMapper();
  }

  @Log
  public Optional<SchoolModel> findBy(String id)
      throws IOException {

    Optional<Auditable<School>> schoolOptional
        = schoolRepository.findBy(id);

    if (schoolOptional.isPresent()) {
      Auditable<School> school = schoolOptional.get();
      SchoolModel schoolModel = schoolModelMapper.from(school);
      return Optional.of(schoolModel);
    }

    return Optional.empty();
  }
}