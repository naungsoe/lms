package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 5/11/16.
 */
@IndexCollection(namespace = "lms", name = "attempts")
public abstract class ResourceAttempt
    extends Auditable implements Serializable {

  @IndexField
  protected User user;

  public User getUser() {
    return user;
  }
}