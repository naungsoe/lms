package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.entity.Quiz;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.QuizModel;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class QuizService extends BaseService {

  private final UnitOfWork unitOfWork;

  private final IndexRepository indexRepository;

  @Inject
  QuizService(
      UnitOfWork unitOfWork,
      IndexRepository indexRepository) {

    this.unitOfWork = unitOfWork;
    this.indexRepository = indexRepository;
  }

  @Log
  public Optional<QuizModel> findBy(String id)
      throws IOException {

    return findBy(id, Configuration.create());
  }

  @Log
  public Optional<QuizModel> findBy(
      String id, Configuration configuration)
      throws IOException {

    long timestamp = unitOfWork.getTimestamp(id);

    if (timestamp == Long.MIN_VALUE) {
      return Optional.empty();
    }

    Optional<Quiz> quizOptional
        = indexRepository.findBy(id, Quiz.class);

    if (quizOptional.isPresent()) {
      Quiz quiz = quizOptional.get();
      QuizModel model = getModel(quiz,
          QuizModel.class, configuration);
      return Optional.of(model);
    }

    return Optional.empty();
  }
}
