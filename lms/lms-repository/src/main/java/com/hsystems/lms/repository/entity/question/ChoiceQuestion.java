package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.ListUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public class ChoiceQuestion extends Question {

  private static final long serialVersionUID = -4884225998143529748L;

  @IndexField(type = IndexFieldType.LIST)
  private List<QuestionOption> options;

  ChoiceQuestion() {

  }

  public ChoiceQuestion(
      String id,
      String body,
      String hint,
      String explanation,
      List<QuestionOption> options) {

    this.id = id;
    this.body = body;
    this.hint = hint;
    this.explanation = explanation;
    this.options = options;
  }

  public ChoiceQuestion(
      String id,
      QuestionType type,
      String body,
      String hint,
      String explanation,
      List<QuestionOption> options,
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

  public QuestionType getType() {
    return type;
  }

  public String getBody() {
    return body;
  }

  public String getHint() {
    return hint;
  }

  public String getExplanation() {
    return explanation;
  }

  public List<QuestionOption> getOptions() {
    return ListUtils.isEmpty(options)
        ? Collections.emptyList()
        : Collections.unmodifiableList(options);
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

    ChoiceQuestion question = (ChoiceQuestion) obj;
    return id.equals(question.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "Question{id=%s, type=%s, body=%s, hint=%s, explanation=%s, "
            + "options=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, type, body, hint, explanation, StringUtils.join(options, ","),
        createdBy, createdDateTime, modifiedBy, modifiedDateTime);
  }
}
