package com.hsystems.lms.question.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class QuestionComponentModel<T extends QuestionModel>
    extends ComponentModel implements Serializable {

  private static final long serialVersionUID = -1240118758104113204L;

  private String id;

  private T question;

  private long score;

  public QuestionComponentModel() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public T getQuestion() {
    return question;
  }

  public void setQuestion(T question) {
    this.question = question;
  }

  public long getScore() {
    return score;
  }

  public void setScore(long score) {
    this.score = score;
  }
}