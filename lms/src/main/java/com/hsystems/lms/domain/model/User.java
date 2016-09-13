package com.hsystems.lms.domain.model;

import java.time.LocalDate;

/**
 * Created by administrator on 8/8/16.
 */
public class User {

  protected String id;

  protected String password;

  protected String salt;

  protected String firstName;

  protected String lastName;

  protected LocalDate birthday;

  protected String gender;

  protected String mobile;

  protected String email;

  protected User() {

  }

  public User(
      String id,
      String password,
      String salt,
      String firstName,
      String lastName,
      LocalDate birthday,
      String gender,
      String mobile,
      String email) {

    this.id = id;
    this.password = password;
    this.salt = salt;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.gender = gender;
    this.mobile = mobile;
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }

  public String getSalt() { return salt; }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public LocalDate getBirthday() {
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
}
