package com.hsystems.lms.lesson.repository.entity;

import com.hsystems.lms.component.Component;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class LessonComponent implements Component, Serializable {

  private static final long serialVersionUID = 3259207023149240903L;

  private String id;

  private Lesson lesson;

  LessonComponent() {

  }

  public LessonComponent(
      String id,
      Lesson lesson) {

    this.id = id;
    this.lesson = lesson;
  }

  @Override
  public String getId() {
    return id;
  }

  public Lesson getLesson() {
    return lesson;
  }
}