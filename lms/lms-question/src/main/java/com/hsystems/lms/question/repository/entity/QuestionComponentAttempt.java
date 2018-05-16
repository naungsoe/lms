package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.component.GradableComponentAttempt;

import java.io.Serializable;

public final class QuestionComponentAttempt<T extends Question>
    implements GradableComponentAttempt<QuestionComponent<T>>, Serializable {

  private static final long serialVersionUID = -1186752398185395475L;

  private String id;

  private QuestionComponent<T> component;

  private QuestionAttempt<T> attempt;

  private QuestionGradingStrategy<T> strategy;

  private long score;

  QuestionComponentAttempt() {

  }

  public QuestionComponentAttempt(
      String id,
      QuestionComponent<T> component,
      QuestionAttempt<T> attempt,
      QuestionGradingStrategy<T> strategy,
      long score) {

    this.id = id;
    this.component = component;
    this.attempt = attempt;
    this.strategy = strategy;
    this.score = score;
  }

  public String getId() {
    return id;
  }

  @Override
  public QuestionComponent<T> getComponent() {
    return component;
  }

  public QuestionAttempt<T> getAttempt() {
    return attempt;
  }

  public long getScore() {
    return score;
  }

  void setScore(long score) {
    this.score = score;
  }

  @Override
  public void gradeAttempt() {
    strategy.gradeAttempt(this);
  }
}