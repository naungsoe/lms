package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.model.Question;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.service.annotation.Log;
import com.hsystems.lms.service.exception.ServiceException;

import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
@Singleton
public class QuestionService {

  private QuestionRepository questionRepository;

  private AuditLogRepository auditLogRepository;

  private ShareLogRepository shareLogRepository;

  @Inject
  QuestionService(
      QuestionRepository questionRepository,
      AuditLogRepository auditLogRepository,
      ShareLogRepository shareLogRepository) {

    this.questionRepository = questionRepository;
    this.auditLogRepository = auditLogRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @Log
  public Optional<Question> findBy(String id)
      throws ServiceException {

    try {
      return questionRepository.findBy(id);

    } catch (RepositoryException e) {
      throw new ServiceException("error retrieving school", e);
    }
  }
}
