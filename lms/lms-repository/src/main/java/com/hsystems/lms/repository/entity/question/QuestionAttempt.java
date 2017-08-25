package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 6/1/17.
 */
public abstract class QuestionAttempt implements Serializable {

  @IndexField
  protected String id;

  public String getId() {
    return id;
  }
}