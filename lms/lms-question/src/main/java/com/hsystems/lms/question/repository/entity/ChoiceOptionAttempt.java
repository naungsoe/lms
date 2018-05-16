package com.hsystems.lms.question.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 1/11/16.
 */
public final class ChoiceOptionAttempt implements Serializable {

  private static final long serialVersionUID = 2979521756188967210L;

  private String id;

  private ChoiceOption option;

  ChoiceOptionAttempt() {

  }

  public ChoiceOptionAttempt(
      String id,
      ChoiceOption option) {

    this.id = id;
    this.option = option;
  }

  public String getId() {
    return id;
  }

  public ChoiceOption getOption() {
    return option;
  }

  public boolean isCorrect() {
    return id.equals(option.getId())
        && option.isCorrect();
  }
}