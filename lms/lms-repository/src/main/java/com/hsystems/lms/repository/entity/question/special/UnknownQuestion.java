package com.hsystems.lms.repository.entity.question.special;

import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionGradingStrategy;

/**
 * Created by administrator on 24/5/17.
 */
public final class UnknownQuestion implements Question {

  public UnknownQuestion() {

  }

  @Override
  public String getBody() {
    return "";
  }

  @Override
  public String getHint() {
    return "";
  }

  @Override
  public String getExplanation() {
    return "";
  }

  @Override
  public QuestionGradingStrategy getGradingStrategy() {
    return new UnknownQuestionGradingStrategy(this);
  }
}
