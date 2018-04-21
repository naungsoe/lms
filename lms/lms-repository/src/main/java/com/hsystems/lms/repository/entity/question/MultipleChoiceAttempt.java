package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;

/**
 * Created by naungsoe on 5/11/16.
 */
public final class MultipleChoiceAttempt implements QuestionAttempt {

  private static final long serialVersionUID = 1665971725030339278L;

  @IndexField
  private ChoiceOptionAttempt optionAttempt;

  MultipleChoiceAttempt() {

  }

  public MultipleChoiceAttempt(ChoiceOptionAttempt optionAttempt) {
    this.optionAttempt = optionAttempt;
  }

  public ChoiceOptionAttempt getOptionAttempt() {
    return optionAttempt;
  }
}
