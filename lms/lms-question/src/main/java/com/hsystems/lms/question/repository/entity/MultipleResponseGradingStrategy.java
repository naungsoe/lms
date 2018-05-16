package com.hsystems.lms.question.repository.entity;

import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by naungsoe on 6/1/17.
 */
public final class MultipleResponseGradingStrategy
    implements QuestionGradingStrategy<MultipleResponse> {

  public MultipleResponseGradingStrategy() {

  }

  @Override
  public void gradeAttempt(
      QuestionComponentAttempt<MultipleResponse> componentAttempt) {

    QuestionComponent<MultipleResponse> questionComponent
        = componentAttempt.getComponent();
    long correctOptionCount = getCorrectOptionCount(questionComponent);
    long correctOptionAttemptCount = getCorrectOptionAttempt(componentAttempt);

    if (correctOptionAttemptCount == correctOptionCount) {
      long score = questionComponent.getScore();
      componentAttempt.setScore(score);
    }
  }

  private long getCorrectOptionCount(
      QuestionComponent<MultipleResponse> questionComponent) {

    MultipleResponse question = questionComponent.getQuestion();
    Enumeration<ChoiceOption> enumeration = question.getOptions();
    return Collections.list(enumeration).stream()
        .filter(element -> element.isCorrect()).count();
  }

  private long getCorrectOptionAttempt(
      QuestionComponentAttempt<MultipleResponse> componentAttempt) {

    MultipleResponseAttempt questionAttempt
        = (MultipleResponseAttempt) componentAttempt.getAttempt();
    Enumeration<ChoiceOptionAttempt> enumeration
        = questionAttempt.getOptionAttempts();
    return Collections.list(enumeration).stream()
        .filter(element -> element.isCorrect()).count();
  }
}