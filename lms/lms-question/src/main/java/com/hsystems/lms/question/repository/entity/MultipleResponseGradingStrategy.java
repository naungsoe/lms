package com.hsystems.lms.question.repository.entity;

import java.util.Enumeration;

/**
 * Created by naungsoe on 6/1/17.
 */
public final class MultipleResponseGradingStrategy
    implements QuestionGradingStrategy<QuestionComponentAttempt> {

  private MultipleResponseComponent component;

  MultipleResponseGradingStrategy() {

  }

  public MultipleResponseGradingStrategy(MultipleResponseComponent component) {
    this.component = component;
  }

  @Override
  public void gradeAttempt(QuestionComponentAttempt componentAttempt) {
    QuestionAttempt questionAttempt = componentAttempt.getAttempt();
    MultipleResponseAttempt attempt = (MultipleResponseAttempt) questionAttempt;
    Enumeration<ChoiceOptionAttempt> enumeration = attempt.getOptionAttempts();

    while (enumeration.hasMoreElements()) {
      ChoiceOptionAttempt element = enumeration.nextElement();
      gradeOptionAttempt(element);
    }
  }

  private void gradeOptionAttempt(ChoiceOptionAttempt optionAttempt) {
    MultipleResponse question = component.getQuestion();
    Enumeration<ChoiceOption> enumeration = question.getOptions();

    while (enumeration.hasMoreElements()) {
      ChoiceOption element = enumeration.nextElement();

      if (element.getId().equals(optionAttempt.getId())) {
        optionAttempt.setCorrect(element.isCorrect());
      }
    }
  }

  @Override
  public long calculateScore(QuestionComponentAttempt componentAttempt) {
    QuestionAttempt questionAttempt = componentAttempt.getAttempt();
    MultipleResponseAttempt attempt = (MultipleResponseAttempt) questionAttempt;
    Enumeration<ChoiceOptionAttempt> enumeration = attempt.getOptionAttempts();

    while (enumeration.hasMoreElements()) {
      ChoiceOptionAttempt element = enumeration.nextElement();

      if (!element.isCorrect()) {
        return 0;
      }
    }

    int correctOptionCount = getCorrectOptionCount();
    int correctOptionAttemptCount
        = getCorrectOptionAttemptCount(componentAttempt);
    return (correctOptionCount == correctOptionAttemptCount)
        ? component.getScore() : 0;
  }

  private int getCorrectOptionCount() {
    MultipleResponse question = component.getQuestion();
    Enumeration<ChoiceOption> enumeration = question.getOptions();
    int correctOptionCount = 0;

    while (enumeration.hasMoreElements()) {
      ChoiceOption element = enumeration.nextElement();

      if (element.isCorrect()) {
        correctOptionCount++;
      }
    }

    return correctOptionCount;
  }

  private int getCorrectOptionAttemptCount(
      QuestionComponentAttempt componentAttempt) {

    QuestionAttempt questionAttempt = componentAttempt.getAttempt();
    MultipleResponseAttempt attempt = (MultipleResponseAttempt) questionAttempt;
    Enumeration<ChoiceOptionAttempt> enumeration = attempt.getOptionAttempts();
    int correctOptionCount = 0;

    while (enumeration.hasMoreElements()) {
      ChoiceOptionAttempt element = enumeration.nextElement();

      if (element.isCorrect()) {
        correctOptionCount++;
      }
    }

    return correctOptionCount;
  }
}