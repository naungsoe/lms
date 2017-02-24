package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.service.model.SchoolModel;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class SchoolService extends BaseService {

  private final IndexRepository indexRepository;

  @Inject
  SchoolService(
      IndexRepository indexRepository) {

    this.indexRepository = indexRepository;
  }

  @Log
  public Optional<SchoolModel> findBy(String id)
      throws IOException {

    Optional<School> schoolOptional
        = indexRepository.findBy(id, School.class);

    if (schoolOptional.isPresent()) {
      School school = schoolOptional.get();
      SchoolModel model = getModel(school, SchoolModel.class);
      return Optional.of(model);
    }

    return Optional.empty();
  }
}
