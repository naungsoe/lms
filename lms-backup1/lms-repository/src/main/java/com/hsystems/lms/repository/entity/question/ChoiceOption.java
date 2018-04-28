package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 1/11/16.
 */
public final class ChoiceOption implements Serializable {

  private static final long serialVersionUID = 8261068830688432126L;

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

  ChoiceOption() {

  }

  public ChoiceOption(
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
}
