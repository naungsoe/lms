package com.hsystems.lms.question.repository.entity.special;

import com.hsystems.lms.question.repository.entity.QuestionComponent;
import com.hsystems.lms.question.repository.entity.QuestionGradingStrategy;

import java.io.Serializable;

/**
 * Created by administrator on 24/5/17.
 */
public final class UnknownQuestionComponent
    implements QuestionComponent<UnknownQuestion>, Serializable {

  private static final long serialVersionUID = -7180665853460910903L;

  public UnknownQuestionComponent() {

  }

  public String getId() {
    return "";
  }

  @Override
  public UnknownQuestion getQuestion() {
    return new UnknownQuestion();
  }

  @Override
  public long getScore() {
    return 0;
  }

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public QuestionGradingStrategy getGradingStrategy() {
    return new UnknownQuestionGradingStrategy();
  }
}