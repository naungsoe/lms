package com.hsystems.lms.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Created by administrator on 8/8/16.
 */
public class User implements Serializable {

  private static final long serialVersionUID = -3039577050861410422L;

  protected String id;

  protected String password;

  protected String salt;

  protected String firstName;

  protected String lastName;

  protected LocalDate dateOfBirth;

  protected String gender;

  protected String mobile;

  protected String email;

  protected String locale;

  protected List<Permission> permissions;

  protected School school;

  protected List<Group> groups;

  User() {

  }

  public User(
      String id,
      String password,
      String salt,
      String firstName,
      String lastName,
      LocalDate dateOfBirth,
      String gender,
      String mobile,
      String email,
      String locale,
      List<Permission> permissions,
      School school,
      List<Group> groups) {

    this.id = id;
    this.password = password;
    this.salt = salt;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.gender = gender;
    this.mobile = mobile;
    this.email = email;
    this.locale = locale;
    this.permissions = permissions;
    this.school = school;
    this.groups = groups;
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

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
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

  public String getLocale() {
    return locale;
  }

  public List<Permission> getPermissions() {
    return Collections.unmodifiableList(permissions);
  }

  public School getSchool() {
    return school;
  }

  public List<Group> getGroups() {
    return Collections.unmodifiableList(groups);
  }
}
