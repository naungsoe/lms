package com.hsystems.lms.service;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.beans.ActivityComponentBean;
import com.hsystems.lms.repository.entity.beans.ComponentBean;
import com.hsystems.lms.repository.entity.beans.ContentComponentBean;
import com.hsystems.lms.repository.entity.beans.FileComponentBean;
import com.hsystems.lms.repository.entity.beans.LessonComponentBean;
import com.hsystems.lms.repository.entity.beans.QuestionComponentBean;
import com.hsystems.lms.repository.entity.beans.QuizComponentBean;
import com.hsystems.lms.repository.entity.beans.SectionComponentBean;
import com.hsystems.lms.repository.entity.beans.TopicComponentBean;
import com.hsystems.lms.repository.entity.course.TopicComponent;
import com.hsystems.lms.repository.entity.file.FileComponent;
import com.hsystems.lms.repository.entity.lesson.ActivityComponent;
import com.hsystems.lms.repository.entity.lesson.ContentComponent;
import com.hsystems.lms.repository.entity.lesson.LessonComponent;
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.CompositeQuestionComponent;
import com.hsystems.lms.repository.entity.question.MultipleChoice;
import com.hsystems.lms.repository.entity.question.MultipleChoiceComponent;
import com.hsystems.lms.repository.entity.question.MultipleResponse;
import com.hsystems.lms.repository.entity.question.MultipleResponseComponent;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.question.special.UnknownQuestionComponent;
import com.hsystems.lms.repository.entity.quiz.QuizComponent;
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
      if (componentBean instanceof TopicComponentBean) {
        TopicComponent topicComponent
            = getTopicComponent(componentBean, componentBeans);
        components.add(topicComponent);

      } else if (componentBean instanceof ActivityComponentBean) {
        ActivityComponent activityComponent
            = getActivityComponent(componentBean, componentBeans);
        components.add(activityComponent);

      } else if (componentBean instanceof SectionComponentBean) {
        SectionComponent sectionComponent
            = getSectionComponent(componentBean, componentBeans);
        components.add(sectionComponent);
      }
    });

    return components;
  }

  protected TopicComponent getTopicComponent(
      ComponentBean componentBean, List<ComponentBean> componentBeans) {

    TopicComponentBean topicComponentBean
        = (TopicComponentBean) componentBean;
    String topicId = topicComponentBean.getId();
    List<Component> resourceComponents
        = getComponents(componentBeans, topicId);

    TopicComponent topicComponent = new TopicComponent(
        topicComponentBean.getId(),
        topicComponentBean.getTitle(),
        topicComponentBean.getInstructions(),
        topicComponentBean.getOrder(),
        resourceComponents
    );
    return topicComponent;
  }

  protected ActivityComponent getActivityComponent(
      ComponentBean componentBean, List<ComponentBean> componentBeans) {

    ActivityComponentBean activityComponentBean
        = (ActivityComponentBean) componentBean;
    String activityId = activityComponentBean.getId();
    List<Component> resourceComponents
        = getComponents(componentBeans, activityId);

    ActivityComponent activityComponent = new ActivityComponent(
        activityComponentBean.getId(),
        activityComponentBean.getTitle(),
        activityComponentBean.getInstructions(),
        activityComponentBean.getOrder(),
        resourceComponents
    );
    return activityComponent;
  }

  private SectionComponent getSectionComponent(
      ComponentBean componentBean, List<ComponentBean> componentBeans) {

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
    return sectionComponent;
  }

  protected List<Component> getComponents(
      List<ComponentBean> componentBeans, String parentId) {

    List<Component> childComponents = new ArrayList<>();

    componentBeans.forEach(componentBean -> {
      String parentBeanId = componentBean.getParentId();

      if (parentBeanId.equals(parentId)) {
        if (componentBean instanceof LessonComponentBean) {
          LessonComponent lessonComponent
              = getLessonComponent(componentBean);
          childComponents.add(lessonComponent);

        } else if (componentBean instanceof QuizComponentBean) {
          QuizComponent quizComponent
              = getQuizComponent(componentBean);
          childComponents.add(quizComponent);

        } else if (componentBean instanceof QuestionComponentBean) {
          QuestionComponent questionComponent
              = getQuestionComponent(componentBean);
          childComponents.add(questionComponent);

        } else if (componentBean instanceof FileComponentBean) {
          FileComponent fileComponent
              = getFileComponent(componentBean);
          childComponents.add(fileComponent);

        } else if (componentBean instanceof ContentComponentBean) {
          ContentComponent contentComponent
              = getContentComponent(componentBean);
          childComponents.add(contentComponent);
        }
      }
    });

    return childComponents;
  }

  protected LessonComponent getLessonComponent(ComponentBean componentBean) {
    LessonComponentBean lessonComponentBean
        = (LessonComponentBean) componentBean;
    LessonComponent lessonComponent = new LessonComponent(
        lessonComponentBean.getId(),
        lessonComponentBean.getLesson(),
        lessonComponentBean.getOrder(),
        lessonComponentBean.getResourceId()
    );
    return lessonComponent;
  }

  protected QuizComponent getQuizComponent(ComponentBean componentBean) {
    QuizComponentBean quizComponentBean = (QuizComponentBean) componentBean;
    QuizComponent quizComponent = new QuizComponent(
        quizComponentBean.getId(),
        quizComponentBean.getQuiz(),
        quizComponentBean.getOrder(),
        quizComponentBean.getResourceId()
    );
    return quizComponent;
  }

  protected QuestionComponent getQuestionComponent(
      ComponentBean componentBean) {

    QuestionComponentBean questionComponentBean
        = (QuestionComponentBean) componentBean;
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

    return questionComponent;
  }

  protected FileComponent getFileComponent(ComponentBean componentBean) {
    FileComponentBean fileComponentBean = (FileComponentBean) componentBean;
    FileComponent fileComponent = new FileComponent(
        fileComponentBean.getId(),
        fileComponentBean.getFileObject(),
        fileComponentBean.getOrder()
    );
    return fileComponent;
  }

  protected ContentComponent getContentComponent(ComponentBean componentBean) {
    ContentComponentBean contentComponentBean
        = (ContentComponentBean) componentBean;
    ContentComponent contentComponent = new ContentComponent(
        contentComponentBean.getId(),
        contentComponentBean.getContent(),
        contentComponentBean.getOrder()
    );
    return contentComponent;
  }

  protected <T, S> S getModel(T entity, Class<S> type) {
    return getModel(entity, type, Configuration.create());
  }

  protected <T, S> S getModel(
      T entity, Class<S> type, Configuration configuration) {

    ModelMapper mapper = ModelMapper.getInstance();
    return mapper.map(entity, type, configuration);
  }

  protected <T, S> S getEntity(T model, Class<S> type) {
    return getEntity(model, type, Configuration.create());
  }

  protected <T, S> S getEntity(
      T model, Class<S> type, Configuration configuration) {

    EntityMapper mapper = EntityMapper.getInstance();
    return mapper.map(model, type, configuration);
  }
}