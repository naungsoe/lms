package com.hsystems.lms.service.model;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
public class QuizComponentModel
    extends ComponentModel implements Serializable {

  private static final long serialVersionUID = 9193038920839977696L;

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
