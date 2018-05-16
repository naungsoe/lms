package com.hsystems.lms.quiz.repository.entity;

import com.hsystems.lms.component.Component;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class QuizComponent implements Component, Serializable {

  private static final long serialVersionUID = -2897687764632967500L;

  private String id;

  private Quiz quiz;

  private int order;

  QuizComponent() {

  }

  public QuizComponent(
      String id,
      Quiz quiz,
      int order) {

    this.id = id;
    this.quiz = quiz;
    this.order = order;
  }

  @Override
  public String getId() {
    return id;
  }

  public Quiz getQuiz() {
    return quiz;
  }

  @Override
  public int getOrder() {
    return order;
  }
}