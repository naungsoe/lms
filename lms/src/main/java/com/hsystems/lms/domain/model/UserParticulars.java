package com.hsystems.lms.domain.model;

/**
 * Created by administrator on 8/8/16.
 */
public class UserParticulars {

  protected String firstName;

  protected String lastName;

  protected UserParticulars() {

  }

  public UserParticulars(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }
}
