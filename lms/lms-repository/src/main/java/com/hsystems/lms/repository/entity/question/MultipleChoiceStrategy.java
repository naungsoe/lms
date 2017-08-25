package com.hsystems.lms.repository.entity.question;

import java.util.Enumeration;

/**
 * Created by naungsoe on 6/1/17.
 */
public class MultipleChoiceStrategy implements GradingStrategy {

  private MultipleChoiceComponent component;

  public MultipleChoiceStrategy(MultipleChoiceComponent component) {
    this.component = component;
  }

  public long gradeAttempt(QuestionComponentAttempt attempt) {
    MultipleChoiceAttempt choiceAttempt = (MultipleChoiceAttempt) attempt;
    String optionId = choiceAttempt.getOptionId();

    MultipleChoice question = (MultipleChoice) component.getQuestion();
    Enumeration<QuestionOption> enumeration = question.getOptions();
    boolean answerCorrect = false;

    while(enumeration.hasMoreElements()) {
      QuestionOption option = enumeration.nextElement();
      answerCorrect = option.isCorrect() && option.getId().equals(optionId);

      if (answerCorrect) {
        break;
      }
    }

    return answerCorrect ? component.getScore() : 0;
  }
}
