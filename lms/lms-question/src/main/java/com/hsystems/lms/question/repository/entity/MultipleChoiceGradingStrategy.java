package com.hsystems.lms.question.repository.entity;

/**
 * Created by naungsoe on 6/1/17.
 */
public final class MultipleChoiceGradingStrategy
    implements QuestionGradingStrategy<MultipleChoice> {

  public MultipleChoiceGradingStrategy() {

  }

  @Override
  public void gradeAttempt(
      QuestionComponentAttempt<MultipleChoice> componentAttempt) {

    QuestionComponent<MultipleChoice> questionComponent
        = componentAttempt.getComponent();
    MultipleChoiceAttempt questionAttempt
        = (MultipleChoiceAttempt) componentAttempt.getAttempt();
    ChoiceOptionAttempt optionAttempt
        = questionAttempt.getOptionAttempt();

    if (optionAttempt.isCorrect()) {
      long score = questionComponent.getScore();
      componentAttempt.setScore(score);
    }
  }
}