package com.hsystems.lms.repository.entity.question.special;

import com.hsystems.lms.repository.entity.question.QuestionGradingStrategy;

public class UnknownQuestionGradingStrategy
    implements QuestionGradingStrategy<UnknownQuestionAttempt> {

  private UnknownQuestion question;

  public UnknownQuestionGradingStrategy(UnknownQuestion question) {
    this.question = question;
  }

  @Override
  public void gradeAttempt(UnknownQuestionAttempt attempt, long maxScore) {

  }

  @Override
  public long getScore() {
    return 0;
  }
}
