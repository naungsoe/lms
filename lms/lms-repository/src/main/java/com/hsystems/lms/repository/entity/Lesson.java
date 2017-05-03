package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@IndexCollection(name = "lessons")
public class Lesson extends Auditable implements Entity, Serializable {

  private static final long serialVersionUID = 885954460906982717L;

  @IndexField
  private String id;

  @IndexField
  private String title;

  @IndexField
  private List<Section> sections;

  Lesson() {

  }

  public Lesson(
      String id,
      String title,
      List<Section> sections) {

    this.id = id;
    this.title = title;
    this.sections = sections;
  }

  public Lesson(
      String id,
      String title,
      List<Section> sections,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.title = title;
    this.sections = sections;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public List<Section> getSections() {
    return Collections.unmodifiableList(sections);
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

    Lesson lesson = (Lesson) obj;
    return id.equals(lesson.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "Lesson{id=%s, title=%s, sections=%s, createdBy=%s, "
            + "createdDateTime=%s, modifiedBy=%s, modifiedDateTime=%s}",
        id, title, StringUtils.join(sections, ","),
        createdBy, createdDateTime, modifiedBy, modifiedDateTime);
  }
}
