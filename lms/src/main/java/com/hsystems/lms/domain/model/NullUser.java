package com.hsystems.lms.domain.model;

/**
 * Created by administrator on 9/8/16.
 */
public final class NullUser extends User implements Null {

  public NullUser() {
    super();
  }

  public boolean isNull() {
    return true;
  }
}
