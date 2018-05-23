package com.hsystems.lms.quiz.repository.entity;

import com.hsystems.lms.component.Component;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class QuizComponent implements Component, Serializable {

  private static final long serialVersionUID = 3939443224016526155L;

  private String id;

  private Quiz quiz;

  QuizComponent() {

  }

  public QuizComponent(
      String id,
      Quiz quiz) {

    this.id = id;
    this.quiz = quiz;
  }

  @Override
  public String getId() {
    return id;
  }

  public Quiz getQuiz() {
    return quiz;
  }
}