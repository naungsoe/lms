package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.repository.entity.Component;

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
    Enumeration<Component> enumeration = question.getComponents();

    while (enumeration.hasMoreElements()) {
      Component component = enumeration.nextElement();
      QuestionComponent questionComponent = (QuestionComponent) component;
      QuestionGradingStrategy gradingStrategy
          = questionComponent.getGradingStrategy();
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
