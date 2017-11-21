package com.hsystems.lms.service;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.beans.ComponentBean;
import com.hsystems.lms.repository.beans.QuestionComponentBean;
import com.hsystems.lms.repository.beans.SectionComponentBean;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.CompositeQuestionComponent;
import com.hsystems.lms.repository.entity.question.MultipleChoice;
import com.hsystems.lms.repository.entity.question.MultipleChoiceComponent;
import com.hsystems.lms.repository.entity.question.MultipleResponse;
import com.hsystems.lms.repository.entity.question.MultipleResponseComponent;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.question.special.UnknownQuestionComponent;
import com.hsystems.lms.repository.entity.quiz.SectionComponent;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.EntityMapper;
import com.hsystems.lms.service.mapper.ModelMapper;
import com.hsystems.lms.service.model.AuditableModel;
import com.hsystems.lms.service.model.UserModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naungsoe on 28/11/16.
 */
public abstract class AbstractService {

  protected final int NUMBER_FOUND_ZERO = 0;

  protected void addSchoolFilter(Query query, Principal principal) {
    UserModel userModel = (UserModel) principal;
    String schoolId = userModel.getSchool().getId();
    query.addCriterion(Criterion.createEqual(
        Constants.FIELD_SCHOOL_ID, schoolId));
  }

  protected void populateCreatedByAndDate(
      AuditableModel auditableModel, Principal principal) {

    String dateTime = DateTimeUtils.toString(LocalDateTime.now(),
        principal.getDateTimeFormat());
    auditableModel.setCreatedBy((UserModel) principal);
    auditableModel.setCreatedDateTime(dateTime);
  }

  protected void populateModifiedByAndDate(
      AuditableModel auditableModel, Principal principal) {

    auditableModel.setModifiedBy((UserModel) principal);
    auditableModel.setModifiedDateTime(DateTimeUtils.toString(
        LocalDateTime.now(), principal.getDateTimeFormat()));
  }

  protected void populatedCreatedDateTime(
      AuditableModel auditableModel, Auditable auditable,
      Configuration configuration) {

    String timeFormat = configuration.getTimeFormat();
    String dateFormat = configuration.getDateFormat();
    LocalDateTime createdDateTime = auditable.getCreatedDateTime();
    String createdTime = DateTimeUtils.toString(createdDateTime, timeFormat);
    String createdDate = DateTimeUtils.toString(createdDateTime, dateFormat);
    auditableModel.setCreatedTime(createdTime);
    auditableModel.setCreatedDate(createdDate);
  }

  protected void populatedModifiedDateTime(
      AuditableModel auditableModel, Auditable auditable,
      Configuration configuration) {

    String timeFormat = configuration.getTimeFormat();
    String dateFormat = configuration.getDateFormat();
    LocalDateTime modifiedDateTime = auditable.getModifiedDateTime();
    String modifiedTime = DateTimeUtils.toString(modifiedDateTime, timeFormat);
    String modifiedDate = DateTimeUtils.toString(modifiedDateTime, dateFormat);
    auditableModel.setCreatedTime(modifiedTime);
    auditableModel.setCreatedDate(modifiedDate);
  }

  protected List<Component> getComponents(
      List<ComponentBean> componentBeans) {

    List<Component> components = new ArrayList<>();

    componentBeans.forEach(componentBean -> {
      if (componentBean instanceof SectionComponentBean) {
        SectionComponentBean sectionComponentBean
            = (SectionComponentBean) componentBean;
        String sectionId = sectionComponentBean.getId();
        List<Component> questionComponents
            = getComponents(componentBeans, sectionId);

        SectionComponent sectionComponent = new SectionComponent(
            sectionComponentBean.getId(),
            sectionComponentBean.getTitle(),
            sectionComponentBean.getInstructions(),
            sectionComponentBean.getOrder(),
            questionComponents
        );
        components.add(sectionComponent);
      }
    });

    return components;
  }

  protected List<Component> getComponents(
      List<ComponentBean> componentBeans, String parentId) {

    List<Component> questionComponents = new ArrayList<>();

    componentBeans.forEach(componentBean -> {
      if (componentBean instanceof QuestionComponentBean) {
        QuestionComponentBean questionComponentBean
            = (QuestionComponentBean) componentBean;
        String parentBeanId = questionComponentBean.getParentId();

        if (parentBeanId.equals(parentId)) {
          Question question = questionComponentBean.getQuestion();
          QuestionComponent questionComponent;

          if (question instanceof CompositeQuestion) {
            questionComponent = new CompositeQuestionComponent(
                questionComponentBean.getId(),
                (CompositeQuestion) question,
                questionComponentBean.getScore(),
                questionComponentBean.getOrder()
            );
          } else if (question instanceof MultipleChoice) {
            questionComponent = new MultipleChoiceComponent(
                questionComponentBean.getId(),
                (MultipleChoice) question,
                questionComponentBean.getScore(),
                questionComponentBean.getOrder()
            );
          } else if (question instanceof MultipleResponse) {
            questionComponent = new MultipleResponseComponent(
                questionComponentBean.getId(),
                (MultipleResponse) question,
                questionComponentBean.getScore(),
                questionComponentBean.getOrder()
            );
          } else {
            questionComponent = new UnknownQuestionComponent();
          }
          questionComponents.add(questionComponent);
        }
      }
    });

    return questionComponents;
  }

  protected <T, S> S getModel(T entity, Class<S> type) {
    return getModel(entity, type, Configuration.create());
  }

  protected <T, S> S getModel(
      T entity, Class<S> type, Configuration configuration) {

    ModelMapper mapper = new ModelMapper(configuration);
    return mapper.map(entity, type);
  }

  protected <T, S> S getEntity(T model, Class<S> type) {
    return getEntity(model, type, Configuration.create());
  }

  protected <T, S> S getEntity(
      T model, Class<S> type, Configuration configuration) {

    EntityMapper mapper = new EntityMapper(configuration);
    return mapper.map(model, type);
  }
}