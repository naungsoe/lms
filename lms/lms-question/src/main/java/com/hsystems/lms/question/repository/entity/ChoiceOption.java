package com.hsystems.lms.question.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 1/11/16.
 */
public final class ChoiceOption implements Serializable {

  private static final long serialVersionUID = -5470055582376205598L;

  private String id;

  private String body;

  private String feedback;

  private boolean correct;

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