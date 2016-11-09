package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.model.School;
import com.hsystems.lms.service.annotation.Log;
import com.hsystems.lms.service.entity.SchoolEntity;
import com.hsystems.lms.service.exception.ServiceException;

import java.util.Optional;

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
  public Optional<SchoolEntity> findBy(String id)
      throws ServiceException {

    try {
      Optional<School> schoolOptional = schoolRepository.findBy(id);

      if (schoolOptional.isPresent()) {
        return null;
      }

      return Optional.empty();

    } catch (RepositoryException e) {
      throw new ServiceException("error retrieving school", e);
    }
  }
}
