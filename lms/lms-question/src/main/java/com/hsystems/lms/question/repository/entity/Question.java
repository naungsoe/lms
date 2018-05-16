package com.hsystems.lms.question.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
public abstract class Question implements Serializable {

  private static final long serialVersionUID = 1837143759213684149L;

  protected String body;

  protected String hint;

  protected String explanation;

  public String getBody() {
    return body;
  }

  public String getHint() {
    return hint;
  }

  public String getExplanation() {
    return explanation;
  }
}