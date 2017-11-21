package com.hsystems.lms.repository.entity.question;

import java.util.Enumeration;

/**
 * Created by naungsoe on 6/1/17.
 */
public final class MultipleChoiceGradingStrategy
    implements QuestionGradingStrategy<QuestionComponentAttempt> {

  private MultipleChoiceComponent component;

  MultipleChoiceGradingStrategy() {

  }

  public MultipleChoiceGradingStrategy(MultipleChoiceComponent component) {
    this.component = component;
  }

  @Override
  public void gradeAttempt(QuestionComponentAttempt componentAttempt) {
    MultipleChoice question = component.getQuestion();
    Enumeration<ChoiceOption> enumeration = question.getOptions();
    QuestionAttempt questionAttempt = componentAttempt.getAttempt();
    MultipleChoiceAttempt attempt =  (MultipleChoiceAttempt) questionAttempt;
    ChoiceOptionAttempt optionAttempt = attempt.getOptionAttempt();

    while (enumeration.hasMoreElements()) {
      ChoiceOption element = enumeration.nextElement();

      if (optionAttempt.getId().equals(element.getId())) {
        optionAttempt.setCorrect(element.isCorrect());
        break;
      }
    }
  }

  @Override
  public long calculateScore(QuestionComponentAttempt componentAttempt) {
    QuestionAttempt questionAttempt = componentAttempt.getAttempt();
    MultipleChoiceAttempt attempt = (MultipleChoiceAttempt) questionAttempt;
    ChoiceOptionAttempt optionAttempt = attempt.getOptionAttempt();
    return optionAttempt.isCorrect() ? component.getScore() : 0;
  }
}
