package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.QuestionType;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.model.Group;
import com.hsystems.lms.repository.model.Question;
import com.hsystems.lms.repository.model.QuestionOption;
import com.hsystems.lms.repository.model.School;
import com.hsystems.lms.repository.model.User;
import com.hsystems.lms.service.annotation.Log;
import com.hsystems.lms.service.entity.QuestionEntity;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.EntityMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
  public Optional<QuestionEntity> findBy(String id)
      throws ServiceException {

    try {
//      Optional<Question> questionOptional = questionRepository.findBy(id);
//
//      if (questionOptional.isPresent()) {
//        return null;
//      }
//
//      return Optional.empty();

      School school = new School(
          "1",
          "Open School",
          "en_US",
          "",
          "",
          new ArrayList<Permission>()
      );

      User user = new User(
          "1",
          "",
          "",
          "Admin",
          "User",
          LocalDate.now(),
          "M",
          "",
          "",
          "",
          "yyyy-MM-dd",
          "yyyy-MM-dd",
          new ArrayList<Permission>(),
          school,
          new ArrayList<Group>()
      );

      List<QuestionOption> options = new ArrayList<>();
      QuestionOption option = new QuestionOption(
          "1",
          "Option Body",
          "",
          true
      );
      options.add(option);

      Question question = new Question(
          "1",
          QuestionType.Essay,
          "Question Body",
          options,
          school,
          user,
          LocalDateTime.now(),
          user,
          LocalDateTime.now()
      );

      Configuration configuration = Configuration.create(user);
      EntityMapper mapper = new EntityMapper(configuration);
      return Optional.of(mapper.map(mapper, QuestionEntity.class));

    } catch (Exception e) {
      throw new ServiceException("error retrieving school", e);
    }
  }
}
