package com.hsystems.lms.question.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 5/11/16.
 */
public final class MultipleChoiceAttempt
    extends QuestionAttempt<MultipleChoice> implements Serializable {

  private static final long serialVersionUID = -5136860907148149669L;

  private ChoiceOptionAttempt optionAttempt;

  MultipleChoiceAttempt() {

  }

  public MultipleChoiceAttempt(
      MultipleChoice question,
      ChoiceOptionAttempt optionAttempt) {

    this.question = question;
    this.optionAttempt = optionAttempt;
  }

  public ChoiceOptionAttempt getOptionAttempt() {
    return optionAttempt;
  }
}