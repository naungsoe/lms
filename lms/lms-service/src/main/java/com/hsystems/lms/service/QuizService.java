package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.entity.quiz.Quiz;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.QuizModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class QuizService extends BaseService {

  private final QuizRepository quizRepository;

  private final IndexRepository indexRepository;

  @Inject
  QuizService(
      QuizRepository quizRepository,
      IndexRepository indexRepository) {

    this.quizRepository = quizRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public QueryResult<QuizModel> findAllBy(Query query, Principal principal)
      throws IOException {

    addSchoolFilter(query, principal);

    QueryResult<Quiz> queryResult
        = indexRepository.findAllBy(query, Quiz.class);
    List<Quiz> quizzes = queryResult.getItems();

    if (CollectionUtils.isEmpty(quizzes)) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          query.getLimit(),
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(principal);
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        getQuizModels(quizzes, configuration)
    );
  }

  private List<QuizModel> getQuizModels(
      List<Quiz> quizzes, Configuration configuration) {

    List<QuizModel> quizModels = new ArrayList<>();

    for (Quiz quiz : quizzes) {
      QuizModel quizModel = getQuizModel(quiz, configuration);
      LocalDateTime createdDateTime = quiz.getCreatedDateTime();
      LocalDateTime modifiedDateTime = quiz.getModifiedDateTime();

      if (DateTimeUtils.isToday(createdDateTime)) {
        quizModel.setCreatedTime(
            DateTimeUtils.toPrettyTime(createdDateTime));
      }

      if (DateTimeUtils.isNotEmpty(modifiedDateTime)
          && DateTimeUtils.isToday(modifiedDateTime)) {

        quizModel.setModifiedTime(
            DateTimeUtils.toPrettyTime(modifiedDateTime));
      }

      quizModels.add(quizModel);
    }

    return quizModels;
  }

  private QuizModel getQuizModel(
      Quiz quiz, Configuration configuration) {

    QuizModel quizModel = getModel(quiz, QuizModel.class, configuration);
    String dateFormat = configuration.getDateFormat();
    LocalDateTime createdDateTime = quiz.getCreatedDateTime();
    LocalDateTime modifiedDateTime = quiz.getModifiedDateTime();

    quizModel.setCreatedDate(
        DateTimeUtils.toString(createdDateTime, dateFormat));

    if (DateTimeUtils.isNotEmpty(modifiedDateTime)) {
      quizModel.setModifiedDate(DateTimeUtils.toString(
          quiz.getModifiedDateTime(), dateFormat));
    }

    return quizModel;
  }

  @Log
  public Optional<QuizModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<Quiz> quizOptional = indexRepository.findBy(id, Quiz.class);

    if (quizOptional.isPresent()) {
      Quiz quiz = quizOptional.get();
      Configuration configuration = Configuration.create(principal);
      QuizModel quizModel = getQuizModel(quiz, configuration);
      return Optional.of(quizModel);
    }

    return Optional.empty();
  }
}
