package com.hsystems.lms.repository.entity.question;

import java.util.Enumeration;

public class CompositeQuestionGradingStrategy
    implements QuestionGradingStrategy<CompositeQuestionAttempt> {

  private CompositeQuestion question;

  private long score;

  public CompositeQuestionGradingStrategy(CompositeQuestion question) {
    this.question = question;
  }

  @Override
  public void gradeAttempt(CompositeQuestionAttempt attempt, long maxScore) {
    Enumeration<QuestionComponentAttempt> enumeration = attempt.getAttempts();

    while (enumeration.hasMoreElements()) {
      QuestionComponentAttempt element = enumeration.nextElement();
      gradeAttempt(element);
      score += element.getScore();
    }
  }

  public void gradeAttempt(QuestionComponentAttempt attempt) {
    Enumeration<QuestionComponent> enumeration = question.getComponents();

    while (enumeration.hasMoreElements()) {
      QuestionComponent element = enumeration.nextElement();

      if (element.getId().equals(attempt.getId())) {
        element.gradeAttempt(attempt);
      }
    }
  }

  @Override
  public long getScore() {
    return score;
  }
}
