package com.hsystems.lms.repository.entity.special;

import com.hsystems.lms.repository.entity.question.Question;

/**
 * Created by administrator on 24/5/17.
 */
public class UnknownQuestion extends Question {

  public UnknownQuestion() {

  }

  @Override
  public String getId() {
    return "";
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
