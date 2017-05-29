package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.entity.Quiz;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.QuizModel;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class QuizService extends BaseService {

  private final QuizRepository quizRepository;

  private final ComponentRepository componentRepository;

  private final IndexRepository indexRepository;

  @Inject
  QuizService(
      QuizRepository quizRepository,
      ComponentRepository componentRepository,
      IndexRepository indexRepository) {

    this.quizRepository = quizRepository;
    this.componentRepository = componentRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public Optional<QuizModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<Quiz> quizOptional = indexRepository.findBy(id, Quiz.class);

    if (quizOptional.isPresent()) {
      Quiz quiz = quizOptional.get();
      Configuration configuration = Configuration.create(principal);
      QuizModel quizModel = getModel(quiz, QuizModel.class, configuration);
      return Optional.of(quizModel);
    }

    return Optional.empty();
  }
}
