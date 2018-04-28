package com.hsystems.lms.service.model.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.service.model.ResourceModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class QuizResourceModel extends ResourceModel
    implements Serializable {

  private static final long serialVersionUID = -5417506400586496820L;

  private QuizModel quiz;

  public QuizResourceModel() {

  }

  public QuizModel getQuiz() {
    return quiz;
  }

  public void setQuiz(QuizModel quiz) {
    this.quiz = quiz;
  }
}