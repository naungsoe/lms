package com.hsystems.lms.question.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 5/11/16.
 */
public final class MultipleChoiceAttempt
    implements QuestionAttempt<MultipleChoice>, Serializable {

  private static final long serialVersionUID = -3022866520402224013L;

  private MultipleChoice multipleChoice;

  private ChoiceOptionAttempt optionAttempt;

  MultipleChoiceAttempt() {

  }

  public MultipleChoiceAttempt(
      MultipleChoice multipleChoice,
      ChoiceOptionAttempt optionAttempt) {

    this.multipleChoice = multipleChoice;
    this.optionAttempt = optionAttempt;
  }

  @Override
  public MultipleChoice getQuestion() {
    return multipleChoice;
  }

  public ChoiceOptionAttempt getOptionAttempt() {
    return optionAttempt;
  }
}