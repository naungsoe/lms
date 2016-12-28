package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.service.model.SchoolModel;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class SchoolService extends BaseService {

  private final SchoolRepository schoolRepository;

  private final AuditLogRepository auditLogRepository;

  @Inject
  SchoolService(
      SchoolRepository schoolRepository,
      AuditLogRepository auditLogRepository) {

    this.schoolRepository = schoolRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @Log
  public Optional<SchoolModel> findBy(String id)
      throws IOException {

    Optional<School> schoolOptional = schoolRepository.findBy(id);

    if (schoolOptional.isPresent()) {
      return null;
    }

    return Optional.empty();
  }
}
