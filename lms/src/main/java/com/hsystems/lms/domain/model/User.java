package com.hsystems.lms.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

  protected List<String> permissions;

  User() {

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
      String email,
      List<String> permissions) {

    this.id = id;
    this.password = password;
    this.salt = salt;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.gender = gender;
    this.mobile = mobile;
    this.email = email;
    this.permissions = permissions;
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

  public List<String> getPermissions() { return permissions; }
}
