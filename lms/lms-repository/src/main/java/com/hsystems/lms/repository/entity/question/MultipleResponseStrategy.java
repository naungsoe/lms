package com.hsystems.lms.repository.entity.question;

import java.util.Enumeration;

/**
 * Created by naungsoe on 6/1/17.
 */
public class MultipleResponseStrategy implements GradingStrategy {

  private MultipleResponseComponent component;

  public MultipleResponseStrategy(MultipleResponseComponent component) {
    this.component = component;
  }

  public long gradeAttempt(QuestionComponentAttempt attempt) {
    MultipleResponseAttempt choiceAttempt = (MultipleResponseAttempt) attempt;
    Enumeration<String> enumeration = choiceAttempt.getOptionIds();
    boolean allAnswersCorrect = false;

    while(enumeration.hasMoreElements()) {
      String optionId = enumeration.nextElement();
      allAnswersCorrect = isOptionIdCorrect(optionId);

      if (!allAnswersCorrect) {
        break;
      }
    }

    return allAnswersCorrect ? component.getScore() : 0;
  }

  private boolean isOptionIdCorrect(String optionId) {
    MultipleResponse question = (MultipleResponse) component.getQuestion();
    Enumeration<QuestionOption> enumeration = question.getOptions();
    boolean answerCorrect = false;

    while(enumeration.hasMoreElements()) {
      QuestionOption option = enumeration.nextElement();
      answerCorrect = option.isCorrect() && option.getId().equals(optionId);

      if (answerCorrect) {
        break;
      }
    }

    return answerCorrect;
  }
}
