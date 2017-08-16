package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by naungsoe on 4/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionOptionModel implements Serializable {

  private static final long serialVersionUID = 2175757096624150046L;

  private String id;

  private String body;

  private String feedback;

  private boolean correct;

  private int order;

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

  public boolean isCorrect() {
    return correct;
  }

  public void setCorrect(boolean correct) {
    this.correct = correct;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }
}
