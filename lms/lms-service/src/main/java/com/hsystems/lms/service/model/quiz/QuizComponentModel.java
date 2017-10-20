package com.hsystems.lms.service.model.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.service.model.ComponentModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class QuizComponentModel extends ComponentModel
    implements Serializable {

  private static final long serialVersionUID = 4329963959762465930L;

  private QuizModel quiz;

  public QuizComponentModel() {

  }

  public QuizModel getQuiz() {
    return quiz;
  }

  public void setQuiz(QuizModel quiz) {
    this.quiz = quiz;
  }
}
