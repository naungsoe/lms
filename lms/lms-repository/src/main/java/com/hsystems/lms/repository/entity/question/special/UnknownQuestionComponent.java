package com.hsystems.lms.repository.entity.question.special;

import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.question.QuestionGradingStrategy;

/**
 * Created by administrator on 24/5/17.
 */
public final class UnknownQuestionComponent
    implements QuestionComponent<UnknownQuestion> {

  public UnknownQuestionComponent() {

  }

  @Override
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
    return null;
  }
}