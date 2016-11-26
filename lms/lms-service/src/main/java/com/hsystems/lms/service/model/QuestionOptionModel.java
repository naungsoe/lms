package com.hsystems.lms.service.model;

import java.io.Serializable;

/**
 * Created by administrator on 4/11/16.
 */
public class QuestionOptionModel implements Serializable {

  private static final long serialVersionUID = -1625929256059640222L;

  private String id;

  private String body;

  private String feedback;

  private boolean correct;

  public QuestionOptionModel() {

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

  public boolean getCorrect() {
    return correct;
  }

  public void setCorrect(boolean correct) {
    this.correct = correct;
  }
}
