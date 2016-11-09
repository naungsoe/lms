package com.hsystems.lms.repository.model;

import java.io.Serializable;

/**
 * Created by naungsoe on 1/11/16.
 */
public final class QuestionOption implements Serializable {

  private static final long serialVersionUID = -4444753561145528740L;

  private String id;

  private String body;

  private String feedback;

  private boolean correct;

  QuestionOption() {

  }

  public QuestionOption(
      String id,
      String body,
      String feedback,
      boolean correct) {

    this.id = id;
    this.body = body;
    this.feedback = feedback;
    this.correct = correct;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  public boolean isCorrect() {
    return correct;
  }

  public void setCorrect(boolean correct) {
    this.correct = correct;
  }
}
