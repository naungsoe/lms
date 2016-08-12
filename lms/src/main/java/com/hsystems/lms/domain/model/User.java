package com.hsystems.lms.domain.model;

/**
 * Created by administrator on 8/8/16.
 */
public class User {

  private final UserCredentials userCredentials;

  private final UserParticulars userParticulars;

  protected User() {
    this(new UserCredentials("", ""),
        new UserParticulars("", ""));
  }

  public User(UserCredentials userCredentials, 
      UserParticulars userParticulars) {

    this.userCredentials = userCredentials;
    this.userParticulars = userParticulars;
  }

  public UserCredentials getUserCredentials() {
    return userCredentials;
  }

  public UserParticulars getUserParticulars() {
    return userParticulars;
  }
}
