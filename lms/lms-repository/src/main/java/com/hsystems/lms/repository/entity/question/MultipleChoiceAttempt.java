package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 5/11/16.
 */
public final class MultipleChoiceAttempt
    implements QuestionAttempt, Serializable {

  private static final long serialVersionUID = 3345284303319605541L;

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
