package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@IndexCollection(name = "questions")
public class Question extends Auditable implements Entity, Serializable {

  private static final long serialVersionUID = 6004706395678775298L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.STRING)
  private QuestionType type;

  @IndexField(type = IndexFieldType.TEXT_GENERAL)
  private String body;

  @IndexField(type = IndexFieldType.STRING)
  private String hint;

  @IndexField(type = IndexFieldType.STRING)
  private String explanation;

  @IndexField(type = IndexFieldType.LIST)
  private List<QuestionOption> options;

  Question() {

  }

  public Question(
      String id,
      QuestionType type,
      String body,
      String hint,
      String explanation,
      List<QuestionOption> options) {

    this.id = id;
    this.type = type;
    this.body = body;
    this.hint = hint;
    this.explanation = explanation;
    this.options = options;
  }

  public Question(
      String id,
      QuestionType type,
      String body,
      String hint,
      String explanation,
      List<QuestionOption> options,
      School school,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.type = type;
    this.body = body;
    this.hint = hint;
    this.explanation = explanation;
    this.options = options;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  @Override
  public String getId() {
    return id;
  }

  public QuestionType getType() { return type; }

  public String getBody() {
    return body;
  }

  public String getHint() { return hint; }

  public String getExplanation() { return explanation; }

  public List<QuestionOption> getOptions() {
    return Collections.unmodifiableList(options);
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

    Question question = (Question) obj;

    return id.equals(question.getId());
  }

  @Override
  public String toString() {
    StringBuilder optionsBuilder = new StringBuilder();
    options.forEach(option -> optionsBuilder.append(option).append(","));

    return String.format(
        "Question{id=%s, type=%s, body=%s, hint=%s, "
            + "explanation=%s, options=%s}",
        id, type, body, hint, explanation, optionsBuilder);
  }
}
