package com.hsystems.lms.repository.entity.question;

import java.util.Enumeration;

public class CompositeQuestionGradingStrategy
    implements QuestionGradingStrategy<QuestionComponentAttempt> {

  private CompositeQuestionComponent component;

  CompositeQuestionGradingStrategy() {

  }

  public CompositeQuestionGradingStrategy(
      CompositeQuestionComponent component) {

    this.component = component;
  }

  @Override
  public void gradeAttempt(QuestionComponentAttempt componentAttempt) {
    QuestionAttempt questionAttempt = componentAttempt.getAttempt();
    CompositeQuestionAttempt attempt
        = (CompositeQuestionAttempt) questionAttempt;
    Enumeration<QuestionComponentAttempt> enumeration = attempt.getAttempts();

    while (enumeration.hasMoreElements()) {
      QuestionComponentAttempt element = enumeration.nextElement();
      gradeComponentAttempt(element);
    }
  }

  private void gradeComponentAttempt(
      QuestionComponentAttempt componentAttempt) {

    CompositeQuestion question = component.getQuestion();
    Enumeration<QuestionComponent> enumeration = question.getComponents();

    while (enumeration.hasMoreElements()) {
      QuestionComponent component = enumeration.nextElement();
      QuestionGradingStrategy gradingStrategy = component.getGradingStrategy();
      componentAttempt.gradeAttempt(gradingStrategy);
    }
  }

  @Override
  public long calculateScore(QuestionComponentAttempt componentAttempt) {
    QuestionAttempt questionAttempt = componentAttempt.getAttempt();
    CompositeQuestionAttempt attempt
        = (CompositeQuestionAttempt) questionAttempt;
    Enumeration<QuestionComponentAttempt> enumeration = attempt.getAttempts();
    long totalScore = 0;

    while (enumeration.hasMoreElements()) {
      QuestionComponentAttempt element = enumeration.nextElement();
      totalScore += element.getScore();
    }

    return totalScore;
  }
}
