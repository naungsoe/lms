package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 1/11/16.
 */
public class QuestionOption implements Serializable {

  private static final long serialVersionUID = -1648329280336973343L;

  @IndexField
  private String id;

  @IndexField
  private String body;

  @IndexField
  private String feedback;

  @IndexField
  private boolean correct;

  @IndexField
  private int order;

  QuestionOption() {

  }

  public QuestionOption(
      String id,
      String body,
      String feedback,
      boolean correct,
      int order) {

    this.id = id;
    this.body = body;
    this.feedback = feedback;
    this.correct = correct;
    this.order = order;
  }

  public String getId() {
    return id;
  }

  public String getBody() {
    return body;
  }

  public String getFeedback() {
    return feedback;
  }

  public boolean isCorrect() {
    return correct;
  }

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

    QuestionOption option = (QuestionOption) obj;
    return id.equals(option.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "Question{id=%s, body=%s, feedback=%s, correct=%s, order=%s}",
        id, body, feedback, correct, order);
  }
}
