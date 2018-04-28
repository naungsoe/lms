package com.hsystems.lms.question.repository.entity.special;

import com.hsystems.lms.question.repository.entity.Question;

import java.io.Serializable;

/**
 * Created by administrator on 24/5/17.
 */
public final class UnknownQuestion implements Question, Serializable {

  private static final long serialVersionUID = 7525692319964623224L;

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