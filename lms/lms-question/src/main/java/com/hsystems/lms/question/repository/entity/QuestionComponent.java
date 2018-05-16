package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.component.Component;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class QuestionComponent<T extends Question>
    implements Component, Serializable {

  private static final long serialVersionUID = 738827399526400272L;

  private String id;

  private T question;

  private long score;

  private int order;

  QuestionComponent() {

  }

  public QuestionComponent(
      String id,
      T question,
      long score,
      int order) {

    this.id = id;
    this.question = question;
    this.score = score;
    this.order = order;
  }

  @Override
  public String getId() {
    return id;
  }

  public T getQuestion() {
    return question;
  }

  public long getScore() {
    return score;
  }

  @Override
  public int getOrder() {
    return order;
  }
}