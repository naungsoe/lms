package com.hsystems.lms.repository.entity.question;

import java.util.Enumeration;

/**
 * Created by naungsoe on 6/1/17.
 */
public final class MultipleChoiceGradingStrategy
    implements QuestionGradingStrategy<MultipleChoiceAttempt> {

  private MultipleChoice question;

  private long score;

  public MultipleChoiceGradingStrategy(MultipleChoice question) {
    this.question = question;
  }

  @Override
  public void gradeAttempt(MultipleChoiceAttempt attempt, long maxScore) {
    Enumeration<ChoiceOption> enumeration = question.getOptions();
    ChoiceOptionAttempt optionAttempt = attempt.getOptionAttempt();

    while (enumeration.hasMoreElements()) {
      ChoiceOption element = enumeration.nextElement();

      if (optionAttempt.getId().equals(element.getId())) {
        optionAttempt.setCorrect(element.isCorrect());
        break;
      }
    }

    if (optionAttempt.isCorrect()) {
      score = maxScore;
    }
  }

  @Override
  public long getScore() {
    return score;
  }
}
