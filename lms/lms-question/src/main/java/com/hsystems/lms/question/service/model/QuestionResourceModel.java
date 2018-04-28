package com.hsystems.lms.question.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionResourceModel<T extends QuestionModel>
    implements Serializable {

  private static final long serialVersionUID = 5590127587196438486L;

  private String id;

  private T question;

  public QuestionResourceModel() {

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
}