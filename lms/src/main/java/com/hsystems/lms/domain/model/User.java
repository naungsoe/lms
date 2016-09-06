package com.hsystems.lms.domain.model;

import java.time.LocalDateTime;

/**
 * Created by administrator on 8/8/16.
 */
public class User {

  protected String identity;

  protected String id;

  protected String password;

  protected String firstName;

  protected String lastName;

  protected LocalDateTime birthday;

  protected String gender;

  protected String mobile;

  protected String email;

  protected School school;

  protected User() {

  }

  public User(
      String identity,
      String id,
      String password,
      String firstName,
      String lastName,
      LocalDateTime birthday,
      String gender,
      String mobile,
      String email,
      School school) {

    this.identity = identity;
    this.id = id;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.gender = gender;
    this.mobile = mobile;
    this.email = email;
    this.school = school;
  }

  public String getIdentity() {
    return identity;
  }

  public String getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public LocalDateTime getBirthday() {
    return birthday;
  }

  public String getGender() {
    return gender;
  }

  public String getMobile() {
    return mobile;
  }

  public String getEmail() {
    return email;
  }

  public School getSchool() {
    return school;
  }
}
