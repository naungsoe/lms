package com.hsystems.lms.question.repository.entity.special;

import com.hsystems.lms.question.repository.entity.Question;

import java.io.Serializable;

/**
 * Created by administrator on 24/5/17.
 */
public final class UnknownQuestion
    extends Question implements Serializable {

  private static final long serialVersionUID = 7582630608161451349L;

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