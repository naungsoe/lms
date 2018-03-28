package com.hsystems.lms.service;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Resource;
import com.hsystems.lms.repository.entity.ResourcePermission;
import com.hsystems.lms.repository.entity.User;
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
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.quiz.QuizComponent;
import com.hsystems.lms.repository.entity.quiz.SectionComponent;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.EntityMapper;
import com.hsystems.lms.service.mapper.ModelMapper;
import com.hsystems.lms.service.model.AuditableModel;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 28/11/16.
 */
public abstract class AbstractService {

  private static final String FIELD_SCHOOL_ID = "schoolId";

  protected final int NUMBER_FOUND_ZERO = 0;

  protected void checkViewPermission(
      Resource resource, User user, String errorMessage) {

    Enumeration<ResourcePermission> permissions = resource.getPermissions();
    boolean hasViewPrivilege = false;

    while (permissions.hasMoreElements()) {
      ResourcePermission permission = permissions.nextElement();

      if (permission.getUser().getId().equals(user.getId())) {
        hasViewPrivilege = permission.isViewable();
        break;
      }
    }

    CommonUtils.checkAccessControl(hasViewPrivilege, errorMessage);
  }

  protected void checkEditPermission(
      Resource resource, User user, String errorMessage) {

    Enumeration<ResourcePermission> permissions = resource.getPermissions();
    boolean hasEditPrivilege = false;

    while (permissions.hasMoreElements()) {
      ResourcePermission permission = permissions.nextElement();

      if (permission.getUser().getId().equals(user.getId())) {
        hasEditPrivilege = permission.isEditable();
        break;
      }
    }

    CommonUtils.checkAccessControl(hasEditPrivilege, errorMessage);
  }

  protected void addSchoolFilter(Query query, Principal principal) {
    UserModel userModel = (UserModel) principal;
    String schoolId = userModel.getSchool().getId();
    query.addCriterion(Criterion.createEqual(FIELD_SCHOOL_ID, schoolId));
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

  protected void populateCreatedDateTime(
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

  protected void populateModifiedDateTime(
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

  protected List<Component> getComponents(List<ComponentBean> componentBeans) {
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

    TopicComponentBean topicComponentBean = (TopicComponentBean) componentBean;
    String topicId = topicComponentBean.getId();

    TopicComponent topicComponent = topicComponentBean.getComponent();
    List<Component> childComponents = getComponents(componentBeans, topicId);
    topicComponent.addComponent(childComponents.toArray(new Component[0]));
    return topicComponent;
  }

  protected ActivityComponent getActivityComponent(
      ComponentBean componentBean, List<ComponentBean> componentBeans) {

    ActivityComponentBean activityComponentBean
        = (ActivityComponentBean) componentBean;
    String activityId = activityComponentBean.getId();

    ActivityComponent activityComponent = activityComponentBean.getComponent();
    List<Component> childComponents = getComponents(componentBeans, activityId);
    activityComponent.addComponent(childComponents.toArray(new Component[0]));
    return activityComponent;
  }

  private SectionComponent getSectionComponent(
      ComponentBean componentBean, List<ComponentBean> componentBeans) {

    SectionComponentBean sectionComponentBean
        = (SectionComponentBean) componentBean;
    String sectionId = sectionComponentBean.getId();

    SectionComponent sectionComponent = sectionComponentBean.getComponent();
    List<Component> childComponents = getComponents(componentBeans, sectionId);
    sectionComponent.addComponent(childComponents.toArray(new Component[0]));
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
    return lessonComponentBean.getComponent();
  }

  protected QuizComponent getQuizComponent(ComponentBean componentBean) {
    QuizComponentBean quizComponentBean = (QuizComponentBean) componentBean;
    return quizComponentBean.getComponent();
  }

  protected QuestionComponent getQuestionComponent(
      ComponentBean componentBean) {

    QuestionComponentBean questionComponentBean
        = (QuestionComponentBean) componentBean;
    return questionComponentBean.getComponent();
  }

  protected FileComponent getFileComponent(ComponentBean componentBean) {
    FileComponentBean fileComponentBean = (FileComponentBean) componentBean;
    return fileComponentBean.getComponent();
  }

  protected ContentComponent getContentComponent(ComponentBean componentBean) {
    ContentComponentBean contentComponentBean
        = (ContentComponentBean) componentBean;
    return contentComponentBean.getComponent();
  }

  protected List<ComponentBean> getComponentBeans(
      List<Component> components, String resourceId, String parentId)
      throws IOException {

    List<ComponentBean> componentBeans = new ArrayList<>();

    for (Component component : components) {
      if (component instanceof TopicComponent) {
        TopicComponent topicComponent = (TopicComponent) component;
        String topicId = topicComponent.getId();
        TopicComponentBean topicComponentBean
            = new TopicComponentBean(topicComponent, resourceId, parentId);
        componentBeans.add(topicComponentBean);

        List<Component> childComponents
            = Collections.list(topicComponent.getComponents());
        List<ComponentBean> childComponentBeans
            = getComponentBeans(childComponents, resourceId, topicId);
        componentBeans.addAll(childComponentBeans);

      } else if (component instanceof LessonComponent) {
        LessonComponent lessonComponent = (LessonComponent) component;
        LessonComponentBean lessonComponentBean
            = new LessonComponentBean(lessonComponent, resourceId, parentId);
        componentBeans.add(lessonComponentBean);

      } else if (component instanceof QuizComponent) {
        QuizComponent quizComponent = (QuizComponent) component;
        QuizComponentBean quizComponentBean
            = new QuizComponentBean(quizComponent, resourceId, parentId);
        componentBeans.add(quizComponentBean);

      } else if (component instanceof ActivityComponent) {
        ActivityComponent activityComponent = (ActivityComponent) component;
        String activityId = activityComponent.getId();
        ActivityComponentBean activityComponentBean
            = new ActivityComponentBean(
            activityComponent, resourceId, parentId);
        componentBeans.add(activityComponentBean);

        List<Component> childComponents
            = Collections.list(activityComponent.getComponents());
        List<ComponentBean> childComponentBeans
            = getComponentBeans(childComponents, resourceId, activityId);
        componentBeans.addAll(childComponentBeans);

      } else if (component instanceof SectionComponent) {
        SectionComponent sectionComponent = (SectionComponent) component;
        String sectionId = sectionComponent.getId();
        SectionComponentBean sectionComponentBean
            = new SectionComponentBean(
            sectionComponent, resourceId, parentId);
        componentBeans.add(sectionComponentBean);

        List<Component> childComponents
            = Collections.list(sectionComponent.getComponents());
        List<ComponentBean> childComponentBeans
            = getComponentBeans(childComponents, resourceId, sectionId);
        componentBeans.addAll(childComponentBeans);

      } else if (component instanceof QuestionComponent) {
        QuestionComponent questionComponent = (QuestionComponent) component;
        QuestionComponentBean questionComponentBean
            = new QuestionComponentBean(
                questionComponent, resourceId, parentId);
        componentBeans.add(questionComponentBean);

      } else if (component instanceof ContentComponent) {
        ContentComponent contentComponent = (ContentComponent) component;
        ContentComponentBean contentComponentBean
            = new ContentComponentBean(
                contentComponent, resourceId, parentId);
        componentBeans.add(contentComponentBean);
      }
    }

    return componentBeans;
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