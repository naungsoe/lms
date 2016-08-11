package com.hsystems.lms.domain.model;

/**
 * Created by administrator on 9/8/16.
 */
public final class UserEmpty extends User implements Empty {

  public UserEmpty() {
    
  }

  public boolean isEmpty() {
    return true;
  }
}
