package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
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

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.TEXT_GENERAL)
  private String title;

  @IndexField(type = IndexFieldType.STRING)
  private String instructions;

  @IndexField(type = IndexFieldType.LIST)
  private List<QuizSection> sections;

  Quiz() {

  }

  public Quiz(
      String id,
      String title,
      String instructions,
      List<QuizSection> sections,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.title = title;
    this.instructions = instructions;
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

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public List<QuizSection> getSections() {
    return Collections.unmodifiableList(sections);
  }

  public void setSections(
      List<QuizSection> sections) {
    this.sections = sections;
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
        "Quiz{id=%s, title=%s, instructions=%s, sections=%s, createdBy=%s, "
            + "createdDateTime=%s, modifiedBy=%s, modifiedDateTime=%s}",
        id, title, instructions, StringUtils.join(sections, ","), createdBy,
        createdDateTime, modifiedBy, modifiedDateTime);
  }
}
