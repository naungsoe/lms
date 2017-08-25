package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 5/11/16.
 */
public class MultipleChoiceAttempt extends QuestionComponentAttempt
    implements Serializable {

  private static final long serialVersionUID = -2226333026142212633L;

  @IndexField
  private String optionId;

  MultipleChoiceAttempt() {

  }

  public MultipleChoiceAttempt(
      String id,
      QuestionComponent component,
      String optionId,
      long score) {

    this.id = id;
    this.component = component;
    this.optionId = optionId;
    this.score = score;
  }

  public String getOptionId() {
    return optionId;
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

    MultipleChoiceAttempt attempt = (MultipleChoiceAttempt) obj;
    return id.equals(attempt.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "MultipleResponseAttempt{id=%s, component=%s, optionId=%s, score=%s}",
        id, component,optionId, score);
  }
}
