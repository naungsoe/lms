package com.hsystems.lms.question.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.question.repository.QuestionRepository;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.question.service.mapper.QuestionResourceModelMapper;
import com.hsystems.lms.question.service.model.QuestionResourceModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 15/10/16.
 */
public final class QuestionService {

  private final Provider<Properties> propertiesProvider;

  private final QuestionRepository questionRepository;

  private final QuestionResourceModelMapper questionMapper;

  @Inject
  QuestionService(
      Provider<Properties> propertiesProvider,
      QuestionRepository questionRepository) {

    this.propertiesProvider = propertiesProvider;
    this.questionRepository = questionRepository;
    this.questionMapper = new QuestionResourceModelMapper();
  }

  @Log
  @Requires(QuestionPermission.VIEW_QUESTION)
  public QueryResult<QuestionResourceModel> findAllBy(Query query)
      throws IOException {

    QueryResult<Auditable<QuestionResource>> queryResult
        = questionRepository.findAllBy(query);
    long elapsedTime = queryResult.getElapsedTime();
    long start = queryResult.getStart();
    long numFound = queryResult.getNumFound();
    List<Auditable<QuestionResource>> questions = queryResult.getItems();

    if (CollectionUtils.isEmpty(questions)) {
      return new QueryResult<>(elapsedTime, start,
          numFound, Collections.emptyList());
    }

    List<QuestionResourceModel> questionModels = new ArrayList<>();

    for (Auditable<QuestionResource> question : questions) {
      QuestionResourceModel questionModel = questionMapper.from(question);
      questionModels.add(questionModel);
    }

    return new QueryResult<>(elapsedTime, start,
        numFound, questionModels);
  }

  @Log
  @Requires(QuestionPermission.VIEW_QUESTION)
  public Optional<QuestionResourceModel> findBy(String id)
      throws IOException {

    Optional<Auditable<QuestionResource>> questionOptional
        = questionRepository.findBy(id);

    if (questionOptional.isPresent()) {
      Auditable<QuestionResource>  question = questionOptional.get();
      QuestionResourceModel questionModel = questionMapper.from(question);
      return Optional.of(questionModel);
    }

    return Optional.empty();
  }

  @Log
  @Requires(QuestionPermission.VIEW_QUESTION)
  public List<String> findAllQuestionTypes() {
    Properties properties = propertiesProvider.get();
    String questionTypes = properties.getProperty("question.question.types");
    return Arrays.asList(questionTypes.split(","));
  }

  @Log
  @Requires(QuestionPermission.CREATE_QUESTION)
  public void add(QuestionResourceModel questionModel)
      throws IOException {

  }

  @Log
  @Requires(QuestionPermission.EDIT_QUESTION)
  public void update(QuestionResourceModel questionModel)
      throws IOException {


  }

  @Log
  @Requires(QuestionPermission.EDIT_QUESTION)
  public void delete(String id)
      throws IOException {

    Optional<Auditable<QuestionResource>> questionOptional
        = questionRepository.findBy(id);

    if (questionOptional.isPresent()) {
      Auditable<QuestionResource> question = questionOptional.get();
      //checkDeletePrivilege(questionResource, principal,
      //    "current user is not owner of the resource");
      questionRepository.remove(question);
    }
  }
}