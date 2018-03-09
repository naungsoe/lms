package com.hsystems.lms.repository.entity.lesson;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.Component;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class LessonComponent implements Component, Serializable {

  private static final long serialVersionUID = -3388828775620300853L;

  @IndexField
  private String id;

  @IndexField
  private Lesson lesson;

  @IndexField
  private int order;

  @IndexField
  private String lessonId;

  LessonComponent() {

  }

  public LessonComponent(
      String id,
      Lesson lesson,
      int order,
      String lessonId) {

    this.id = id;
    this.lesson = lesson;
    this.order = order;
    this.lessonId = lessonId;
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
}
