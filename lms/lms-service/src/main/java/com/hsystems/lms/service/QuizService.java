package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.beans.ComponentBean;
import com.hsystems.lms.repository.beans.QuestionComponentBean;
import com.hsystems.lms.repository.beans.SectionComponentBean;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.quiz.Quiz;
import com.hsystems.lms.repository.entity.quiz.QuizResource;
import com.hsystems.lms.repository.entity.quiz.SectionComponent;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.quiz.QuizResourceModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class QuizService extends AbstractService {

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
  public QueryResult<QuizResourceModel> findAllBy(
      Query query, Principal principal)
      throws IOException {

    addSchoolFilter(query, principal);

    QueryResult<QuizResource> queryResult
        = indexRepository.findAllBy(query, QuizResource.class);
    List<QuizResource> quizResources = queryResult.getItems();

    if (CollectionUtils.isEmpty(quizResources)) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          NUMBER_FOUND_ZERO,
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(principal);
    List<QuizResourceModel> resourceModels
        = getQuizResourceModels(quizResources, configuration);
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        resourceModels
    );
  }

  private List<QuizResourceModel> getQuizResourceModels(
      List<QuizResource> quizResources, Configuration configuration) {

    List<QuizResourceModel> resourceModels = new ArrayList<>();

    for (QuizResource quizResource : quizResources) {
      QuizResourceModel resourceModel
          = getQuizResourceModel(quizResource, configuration);
      LocalDateTime createdDateTime = quizResource.getCreatedDateTime();
      LocalDateTime modifiedDateTime = quizResource.getModifiedDateTime();
      String dateFormat = configuration.getDateFormat();

      if (DateTimeUtils.isToday(createdDateTime)) {
        resourceModel.setCreatedTime(
            DateTimeUtils.toPrettyTime(createdDateTime, dateFormat));
      }

      if (DateTimeUtils.isNotEmpty(modifiedDateTime)
          && DateTimeUtils.isToday(modifiedDateTime)) {

        resourceModel.setModifiedTime(
            DateTimeUtils.toPrettyTime(modifiedDateTime, dateFormat));
      }

      resourceModels.add(resourceModel);
    }

    return resourceModels;
  }

  private QuizResourceModel getQuizResourceModel(
      QuizResource quizResource, Configuration configuration) {

    QuizResourceModel resourceModel = getModel(quizResource,
        QuizResourceModel.class, configuration);
    String dateFormat = configuration.getDateFormat();
    LocalDateTime createdDateTime = quizResource.getCreatedDateTime();
    LocalDateTime modifiedDateTime = quizResource.getModifiedDateTime();

    resourceModel.setCreatedDate(
        DateTimeUtils.toString(createdDateTime, dateFormat));

    if (DateTimeUtils.isNotEmpty(modifiedDateTime)) {
      resourceModel.setModifiedDate(DateTimeUtils.toString(
          quizResource.getModifiedDateTime(), dateFormat));
    }

    return resourceModel;
  }

  @Log
  public Optional<QuizResourceModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<QuizResource> resourceOptional
        = indexRepository.findBy(id, QuizResource.class);

    if (resourceOptional.isPresent()) {
      QuizResource quizResource = resourceOptional.get();
      Quiz quiz = quizResource.getQuiz();
      List<Component> sectionComponents = getSectionComponents(id);
      quiz.addComponent(sectionComponents.toArray(new Component[0]));

      Configuration configuration = Configuration.create(principal);
      QuizResourceModel resourceModel
          = getQuizResourceModel(quizResource, configuration);
      return Optional.of(resourceModel);
    }

    return Optional.empty();
  }

  private List<Component> getSectionComponents(String id)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual("resourceId", id));
    QueryResult<ComponentBean> queryResult
        = indexRepository.findAllBy(query, ComponentBean.class);
    List<ComponentBean> componentBeans = queryResult.getItems();

    if (CollectionUtils.isEmpty(componentBeans)) {
      return Collections.emptyList();
    }

    List<Component> sectionComponents = new ArrayList<>();

    componentBeans.forEach(componentBean -> {
      if (componentBean instanceof SectionComponentBean) {
        SectionComponentBean sectionComponentBean
            = (SectionComponentBean) componentBean;
        String sectionId = sectionComponentBean.getId();
        List<Component> questionComponents
            = getQuestionComponents(componentBeans, sectionId);

        SectionComponent sectionComponent = new SectionComponent(
            sectionComponentBean.getId(),
            sectionComponentBean.getTitle(),
            sectionComponentBean.getInstructions(),
            sectionComponentBean.getOrder(),
            questionComponents
        );
        sectionComponents.add(sectionComponent);
      }
    });

    return sectionComponents;
  }

  private List<Component> getQuestionComponents(
      List<ComponentBean> componentBeans, String parentId) {

    List<Component> questionComponents = new ArrayList<>();

    componentBeans.forEach(componentBean -> {
      if (componentBean instanceof QuestionComponentBean) {
        QuestionComponentBean questionComponentBean
            = (QuestionComponentBean) componentBean;
        String parentBeanId = questionComponentBean.getParentId();

        if (parentBeanId.equals(parentId)) {
          QuestionComponent questionComponent = new QuestionComponent(
              questionComponentBean.getId(),
              questionComponentBean.getQuestion(),
              questionComponentBean.getScore(),
              questionComponentBean.getOrder()
          );
          questionComponents.add(questionComponent);
        }
      }
    });

    return questionComponents;
  }
}
