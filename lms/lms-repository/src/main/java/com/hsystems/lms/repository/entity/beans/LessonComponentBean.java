package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.lesson.Lesson;
import com.hsystems.lms.repository.entity.lesson.LessonComponent;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class LessonComponentBean
    implements ComponentBean<LessonComponent>, Serializable {

  private static final long serialVersionUID = -4120183072754254647L;

  @IndexField
  private String id;

  @IndexField
  private Lesson lesson;

  @IndexField
  private int order;

  @IndexField
  private String lessonId;

  @IndexField
  private String resourceId;

  @IndexField
  private String parentId;

  LessonComponentBean() {

  }

  public LessonComponentBean(
      LessonComponent component,
      String resourceId,
      String parentId) {

    this.id = component.getId();
    this.lesson = component.getLesson();
    this.order = component.getOrder();
    this.lessonId = component.getLessonId();
    this.resourceId = resourceId;
    this.parentId = parentId;
  }

  @Override
  public String getId() {
    return id;
  }

  public Lesson getLesson() {
    return lesson;
  }

  @Override
  public int getOrder() {
    return order;
  }

  public String getLessonId() {
    return lessonId;
  }

  @Override
  public String getResourceId() {
    return resourceId;
  }

  @Override
  public String getParentId() {
    return parentId;
  }

  @Override
  public LessonComponent getComponent() {
    return new LessonComponent(id, lesson, order, lessonId);
  }
}
