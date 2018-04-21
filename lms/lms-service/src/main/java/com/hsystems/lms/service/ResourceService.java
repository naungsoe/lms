package com.hsystems.lms.service;

import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Permission;
import com.hsystems.lms.repository.entity.Privilege;
import com.hsystems.lms.repository.entity.Resource;
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
import com.hsystems.lms.service.model.AuditableModel;
import com.hsystems.lms.service.model.UserModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 28/11/16.
 */
public abstract class ResourceService extends AbstractService {

  protected void checkViewPrivilege(
      Resource resource, Principal principal, String errorMessage) {

    List<Privilege> privileges = Arrays.asList(Privilege.EDIT, Privilege.VIEW);
    checkUserPrivilege(resource, principal, privileges, errorMessage);
  }

  protected void checkUserPrivilege(
      Resource resource, Principal principal,
      List<Privilege> privileges, String errorMessage) {

    Enumeration<Permission> permissions = resource.getPermissions();
    boolean hasUserPrivilege = false;

    while (permissions.hasMoreElements()) {
      Permission permission = permissions.nextElement();

      if (permission.getUser().getId().equals(principal.getId())) {
        Privilege userPrivilege = permission.getPrivilege();
        hasUserPrivilege = privileges.contains(userPrivilege);
        break;
      }
    }

    CommonUtils.checkAccessControl(hasUserPrivilege, errorMessage);
  }

  protected void checkEditPrivilege(
      Resource resource, Principal principal, String errorMessage) {


    List<Privilege> privileges = Arrays.asList(Privilege.EDIT);
    checkUserPrivilege(resource, principal, privileges, errorMessage);
  }

  protected void checkDeletePrivilege(
      Resource resource, Principal principal, String errorMessage) {

    UserModel userModel = (UserModel) principal;
    User createdBy = resource.getCreatedBy();
    boolean isOwner = userModel.getId().equals(createdBy.getId());
    CommonUtils.checkArgument(isOwner, errorMessage);
  }

  protected void populateCreatedByAndDate(
      AuditableModel auditableModel, Principal principal) {

    String dateTime = DateTimeUtils.toString(LocalDateTime.now(),
        principal.getDateTimeFormat());
    auditableModel.setCreatedBy((UserModel) principal);
    auditableModel.setCreatedDateTime(dateTime);
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
    auditableModel.setModifiedTime(modifiedTime);
    auditableModel.setModifiedDate(modifiedDate);
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
          LessonComponentBean lessonComponentBean
              = (LessonComponentBean) componentBean;
          LessonComponent lessonComponent
              = lessonComponentBean.getComponent();
          childComponents.add(lessonComponent);

        } else if (componentBean instanceof QuizComponentBean) {
          QuizComponentBean quizComponentBean
              = (QuizComponentBean) componentBean;
          QuizComponent quizComponent
              = quizComponentBean.getComponent();
          childComponents.add(quizComponent);

        } else if (componentBean instanceof QuestionComponentBean) {
          QuestionComponentBean questionComponentBean
              = (QuestionComponentBean) componentBean;
          QuestionComponent questionComponent
              = questionComponentBean.getComponent();
          childComponents.add(questionComponent);

        } else if (componentBean instanceof FileComponentBean) {
          FileComponentBean fileComponentBean
              = (FileComponentBean) componentBean;
          FileComponent fileComponent
              = fileComponentBean.getComponent();
          childComponents.add(fileComponent);

        } else if (componentBean instanceof ContentComponentBean) {
          ContentComponentBean contentComponentBean
              = (ContentComponentBean) componentBean;
          ContentComponent contentComponent
              = contentComponentBean.getComponent();
          childComponents.add(contentComponent);
        }
      }
    });

    return childComponents;
  }
}