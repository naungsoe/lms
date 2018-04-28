package com.hsystems.lms.question.repository.entity.special;

import com.hsystems.lms.question.repository.entity.QuestionComponentAttempt;
import com.hsystems.lms.question.repository.entity.QuestionGradingStrategy;

/**
 * Created by naungsoe on 6/1/17.
 */
public final class UnknownQuestionGradingStrategy
    implements QuestionGradingStrategy<QuestionComponentAttempt> {

  public UnknownQuestionGradingStrategy() {

  }

  @Override
  public void gradeAttempt(QuestionComponentAttempt componentAttempt) {

  }

  @Override
  public long calculateScore(QuestionComponentAttempt componentAttempt) {
    return 0;
  }
}