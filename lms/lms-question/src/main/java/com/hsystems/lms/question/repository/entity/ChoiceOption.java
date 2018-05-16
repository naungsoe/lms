package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.component.Component;

import java.io.Serializable;

/**
 * Created by naungsoe on 1/11/16.
 */
public final class ChoiceOption implements Component, Serializable {

  private static final long serialVersionUID = 2655018708468090646L;

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

  @Override
  public int getOrder() {
    return order;
  }
}