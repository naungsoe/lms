package com.hsystems.lms.repository.entity.question.special;

import com.hsystems.lms.repository.entity.question.Question;

/**
 * Created by administrator on 24/5/17.
 */
public final class UnknownQuestion implements Question {

  private static final long serialVersionUID = -4480430497819510167L;

  public UnknownQuestion() {

  }

  @Override
  public String getBody() {
    return "";
  }

  @Override
  public String getHint() {
    return "";
  }

  @Override
  public String getExplanation() {
    return "";
  }
}