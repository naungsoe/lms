package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public class CompositeQuestionComponent
    implements QuestionComponent<CompositeQuestion>, Serializable {

  private static final long serialVersionUID = 8801021552600205315L;

  @IndexField
  private String id;

  @IndexField
  private CompositeQuestion question;

  @IndexField
  private long score;

  @IndexField
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

  @Override
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