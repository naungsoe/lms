package com.hsystems.lms.question.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class MultipleResponseComponent
    implements QuestionComponent<MultipleResponse>, Serializable {

  private static final long serialVersionUID = -3167930824928926230L;

  private String id;

  private MultipleResponse question;

  private long score;

  private int order;

  MultipleResponseComponent() {

  }

  public MultipleResponseComponent(
      String id,
      MultipleResponse question,
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
  public MultipleResponse getQuestion() {
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
    return new MultipleResponseGradingStrategy(this);
  }
}