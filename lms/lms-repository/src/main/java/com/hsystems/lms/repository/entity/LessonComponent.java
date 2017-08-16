package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public class LessonComponent implements Component, Serializable {

  private static final long serialVersionUID = 1349480384363058632L;

  @IndexField
  private String id;

  @IndexField
  private int order;

  @IndexField
  private Lesson lesson;

  LessonComponent() {

  }

  public LessonComponent(
      String id,
      int order,
      Lesson lesson) {

    this.id = id;
    this.order = order;
    this.lesson = lesson;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public ComponentType getType() {
    return ComponentType.SECTION;
  }

  public Lesson getLesson() {
    return lesson;
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

    LessonComponent section = (LessonComponent) obj;
    return id.equals(section.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "LessonComponent{id=%s, order=%s, lesson=%s}",
        id, order, lesson);
  }
}
