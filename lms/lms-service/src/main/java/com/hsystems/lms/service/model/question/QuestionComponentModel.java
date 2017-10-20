package com.hsystems.lms.service.model.question;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.service.model.ComponentModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionComponentModel<T extends QuestionModel>
    extends ComponentModel implements Serializable {

  private static final long serialVersionUID = 6488637843336854367L;

  private T question;

  public QuestionComponentModel() {

  }

  public T getQuestion() {
    return question;
  }

  public void setQuestion(T question) {
    this.question = question;
  }
}
