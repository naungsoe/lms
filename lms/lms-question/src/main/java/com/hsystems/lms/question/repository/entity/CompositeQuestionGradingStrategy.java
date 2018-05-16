package com.hsystems.lms.question.repository.entity;

import java.util.Enumeration;

public class CompositeQuestionGradingStrategy
    implements QuestionGradingStrategy<CompositeQuestion> {


  public CompositeQuestionGradingStrategy() {

  }

  @Override
  public void gradeAttempt(
      QuestionComponentAttempt<CompositeQuestion> componentAttempt) {

    CompositeQuestionAttempt questionAttempt
        = (CompositeQuestionAttempt) componentAttempt.getAttempt();
    Enumeration<QuestionComponentAttempt> enumeration
        = questionAttempt.getComponentAttempts();
    long totalScore = 0L;

    while (enumeration.hasMoreElements()) {
      QuestionComponentAttempt element = enumeration.nextElement();
      element.gradeAttempt();

      totalScore += element.getScore();
    }

    componentAttempt.setScore(totalScore);
  }
}