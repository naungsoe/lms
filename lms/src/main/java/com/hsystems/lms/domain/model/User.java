package com.hsystems.lms.domain.model;

/**
 * Created by administrator on 8/8/16.
 */
public class User {

  protected String identity;

  protected UserCredentials userCredentials;

  protected UserParticulars userParticulars;

  protected User() {
    this("", new UserCredentials("", ""),
        new UserParticulars("", ""));
  }

  public User(
      String identity,
      UserCredentials userCredentials,
      UserParticulars userParticulars) {

    this.identity = identity;
    this.userCredentials = userCredentials;
    this.userParticulars = userParticulars;
  }

  public String getIdentity() {
    return identity;
  }

  public UserCredentials getUserCredentials() {
    return userCredentials;
  }

  public UserParticulars getUserParticulars() {
    return userParticulars;
  }
}
