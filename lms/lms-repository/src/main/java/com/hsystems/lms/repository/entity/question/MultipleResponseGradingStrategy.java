package com.hsystems.lms.repository.entity.question;

import java.util.Enumeration;

/**
 * Created by naungsoe on 6/1/17.
 */
public final class MultipleResponseGradingStrategy
    implements QuestionGradingStrategy<MultipleResponseAttempt> {

  private MultipleResponse question;

  private long score;

  public MultipleResponseGradingStrategy(MultipleResponse question) {
    this.question = question;
  }

  @Override
  public void gradeAttempt(MultipleResponseAttempt attempt, long maxScore) {
    Enumeration<ChoiceOptionAttempt> enumeration = attempt.getAttempts();
    score = enumeration.hasMoreElements() ? maxScore : 0;

    while (enumeration.hasMoreElements()) {
      ChoiceOptionAttempt element = enumeration.nextElement();
      gradeAttempt(element);
    }

    while (enumeration.hasMoreElements()) {
      ChoiceOptionAttempt element = enumeration.nextElement();

      if (!element.isCorrect()) {
        score = 0;
        break;
      }
    }
  }

  private void gradeAttempt(ChoiceOptionAttempt optionAttempt) {
    Enumeration<ChoiceOption> enumeration = question.getOptions();

    while (enumeration.hasMoreElements()) {
      ChoiceOption element = enumeration.nextElement();

      if (optionAttempt.getId().equals(element.getId())) {
        optionAttempt.setCorrect(element.isCorrect());
        break;
      }
    }
  }

  @Override
  public long getScore() {
    return score;
  }
}