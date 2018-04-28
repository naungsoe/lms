package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.model.School;
import com.hsystems.lms.repository.AuditLogRepository;
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

  private AuditLogRepository auditLogRepository;

  @Inject
  SchoolService(
      SchoolRepository schoolRepository,
      AuditLogRepository auditLogRepository) {

    this.schoolRepository = schoolRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @Log
  public Optional<School> findBy(String id)
      throws ServiceException {

    try {
      return schoolRepository.findBy(id);

    } catch (RepositoryException e) {
      throw new ServiceException("error retrieving school", e);
    }
  }
}
