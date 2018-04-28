package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 1/11/16.
 */
public final class ChoiceOptionAttempt implements Serializable {

  private static final long serialVersionUID = 165188263827952480L;

  @IndexField
  private String id;

  @IndexField
  private boolean correct;

  ChoiceOptionAttempt() {

  }

  public ChoiceOptionAttempt(
      String id,
      boolean correct) {

    this.id = id;
    this.correct = correct;
  }

  public String getId() {
    return id;
  }

  public boolean isCorrect() {
    return correct;
  }

  public void setCorrect(boolean correct) {
    this.correct = correct;
  }
}
