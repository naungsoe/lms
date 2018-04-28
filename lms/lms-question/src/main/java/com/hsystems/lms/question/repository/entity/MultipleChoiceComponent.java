package com.hsystems.lms.question.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class MultipleChoiceComponent
    implements QuestionComponent<MultipleChoice>, Serializable {

  private static final long serialVersionUID = 2942347776136675425L;

  private String id;

  private MultipleChoice question;

  private long score;

  private int order;

  MultipleChoiceComponent() {

  }

  public MultipleChoiceComponent(
      String id,
      MultipleChoice question,
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
  public MultipleChoice getQuestion() {
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
    return new MultipleChoiceGradingStrategy(this);
  }
}