package com.hsystems.lms.service.model;

import com.hsystems.lms.common.Permission;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
public class UserModel implements Serializable {

  private static final long serialVersionUID = -8222871542135593721L;

  private String id;

  private String password;

  private String salt;

  private String firstName;

  private String lastName;

  private String dateOfBirth;

  private String gender;

  private String mobile;

  private String email;

  private String locale;

  private String dateFormat;

  private String dateTimeFormat;

  private List<Permission> permissions;

  private String schoolId;

  private String schoolName;

  private List<UserGroupModel> groups;

  UserModel() {

  }

  public UserModel(
      String id,
      String password,
      String salt,
      String firstName,
      String lastName,
      String dateOfBirth,
      String gender,
      String mobile,
      String email,
      String locale,
      String dateFormat,
      String dateTimeFormat,
      List<Permission> permissions,
      String schoolId,
      String schoolName,
      List<UserGroupModel> groups) {

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
    this.dateFormat = dateFormat;
    this.dateTimeFormat = dateTimeFormat;
    this.permissions = permissions;
    this.schoolId = schoolId;
    this.schoolName = schoolName;
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

  public String getDateOfBirth() {
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

  public String getDateFormat() {
    return dateFormat;
  }

  public String getDateTimeFormat() {
    return dateTimeFormat;
  }

  public List<Permission> getPermissions() {
    return Collections.unmodifiableList(permissions);
  }

  public String getSchoolId() {
    return schoolId;
  }

  public String getSchoolName() {
    return schoolName;
  }

  public List<UserGroupModel> getGroups() {
    return Collections.unmodifiableList(groups);
  }
}
