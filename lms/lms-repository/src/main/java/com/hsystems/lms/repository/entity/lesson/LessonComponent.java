package com.hsystems.lms.repository.entity.lesson;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.Component;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public class LessonComponent implements Component, Serializable {

  private static final long serialVersionUID = 5504807075977704731L;

  @IndexField
  private String id;

  @IndexField
  private Lesson lesson;

  @IndexField
  private int order;

  LessonComponent() {

  }

  public LessonComponent(
      String id,
      Lesson lesson,
      int order) {

    this.id = id;
    this.lesson = lesson;
    this.order = order;
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

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    LessonComponent component = (LessonComponent) obj;
    return id.equals(component.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "LessonComponent{id=%s, lesson=%s, order=%s}",
        id, lesson, order);
  }
}
