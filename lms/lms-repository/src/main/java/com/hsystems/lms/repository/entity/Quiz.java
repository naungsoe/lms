package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
@IndexCollection(name = "quizzes")
public class Quiz extends Auditable implements Entity, Serializable {

  private static final long serialVersionUID = 645532833693995164L;

  @IndexField
  private String id;

  @IndexField
  private String title;

  @IndexField
  private String instructions;

  @IndexField
  private List<Section> sections;

  @IndexField
  private School school;

  Quiz() {

  }

  public Quiz(
      String id,
      String title,
      String instructions,
      List<Section> sections,
      School school,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.title = title;
    this.instructions = instructions;
    this.sections = sections;
    this.school = school;
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

  public String getInstructions() {
    return instructions;
  }

  public List<Section> getSections() {
    return Collections.unmodifiableList(sections);
  }

  public School getSchool() {
    return school;
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

    Quiz quiz = (Quiz) obj;
    return id.equals(quiz.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "Quiz{id=%s, title=%s, instructions=%s, sections=%s, school=%s, "
            + "createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, title, instructions, StringUtils.join(sections, ","), school,
        createdBy, createdDateTime, modifiedBy, modifiedDateTime);
  }
}
