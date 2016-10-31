package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.model.School;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.service.annotation.Log;
import com.hsystems.lms.service.exception.ServiceException;

import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 15/10/16.
 */
@Singleton
public class SchoolService {

  private SchoolRepository schoolRepository;

  @Inject
  SchoolService(
      SchoolRepository schoolRepository) {

    this.schoolRepository = schoolRepository;
  }

  @Log
  public Optional<School> findBy(String key)
      throws ServiceException {

    try {
      return schoolRepository.findBy(key);
    } catch (RepositoryException e) {
      throw new ServiceException(
          "error retrieving school", e);
    }
  }
}
