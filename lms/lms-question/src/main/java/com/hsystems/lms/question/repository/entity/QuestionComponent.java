package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.component.Component;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class QuestionComponent<T extends Question>
    implements Component, Serializable {

  private static final long serialVersionUID = 3062414733689254238L;

  private String id;

  private T question;

  private long score;

  QuestionComponent() {

  }

  public QuestionComponent(
      String id,
      T question,
      long score) {

    this.id = id;
    this.question = question;
    this.score = score;
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
}