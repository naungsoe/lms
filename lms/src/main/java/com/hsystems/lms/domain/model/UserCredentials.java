package com.hsystems.lms.domain.model;

/**
 * Created by administrator on 8/8/16.
 */
public class UserCredentials {

  protected String id;

  protected String password;

  protected UserCredentials() {

  }

  public UserCredentials(String id, String password) {
    this.id = id;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }
}
