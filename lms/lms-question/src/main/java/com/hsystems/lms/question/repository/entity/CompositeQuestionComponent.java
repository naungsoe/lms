package com.hsystems.lms.question.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class CompositeQuestionComponent
    implements QuestionComponent<CompositeQuestion>, Serializable {

  private static final long serialVersionUID = 4860247402302215438L;

  private String id;

  private CompositeQuestion question;

  private long score;

  private int order;

  CompositeQuestionComponent() {

  }

  public CompositeQuestionComponent(
      String id,
      CompositeQuestion question,
      long score,
      int order) {

    this.id = id;
    this.question = question;
    this.score = score;
    this.order = order;
  }

  public String getId() {
    return id;
  }

  @Override
  public CompositeQuestion getQuestion() {
    return question;
  }

  @Override
  public long getScore() {
    return score;
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public QuestionGradingStrategy getGradingStrategy() {
    return new CompositeQuestionGradingStrategy(this);
  }
}