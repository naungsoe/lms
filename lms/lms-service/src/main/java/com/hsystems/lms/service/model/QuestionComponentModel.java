package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionComponentModel
    extends ComponentModel implements Serializable {

  private static final long serialVersionUID = -6712660360345177011L;

  private QuestionModel question;

  public QuestionComponentModel() {

  }

  public QuestionModel getQuestion() {
    return question;
  }

  public void setQuestion(QuestionModel question) {
    this.question = question;
  }
}
