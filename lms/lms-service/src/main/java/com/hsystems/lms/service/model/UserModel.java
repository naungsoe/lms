package com.hsystems.lms.service.model;

import com.hsystems.lms.common.security.Principal;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
public class UserModel implements Principal, Serializable {

  private static final long serialVersionUID = -8222871542135593721L;

  private String id;

  private String firstName;

  private String lastName;

  private String dateOfBirth;

  private String gender;

  private String mobile;

  private String email;

  private String locale;

  private String dateFormat;

  private String dateTimeFormat;

  private List<String> permissions;

  private String schoolId;

  private String schoolName;

  private List<UserGroupModel> groups;

  UserModel() {

  }

  public UserModel(
      String id,
      String firstName,
      String lastName,
      String dateOfBirth,
      String gender,
      String mobile,
      String email,
      String locale,
      String dateFormat,
      String dateTimeFormat,
      List<String> permissions,
      String schoolId,
      String schoolName,
      List<UserGroupModel> groups) {

    this.id = id;
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

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getDateFormat() {
    return dateFormat;
  }

  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  public String getDateTimeFormat() {
    return dateTimeFormat;
  }

  public void setDateTimeFormat(String dateTimeFormat) {
    this.dateTimeFormat = dateTimeFormat;
  }

  public List<String> getPermissions() {
    return Collections.unmodifiableList(permissions);
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public String getSchoolId() {
    return schoolId;
  }

  public void setSchoolId(String schoolId) {
    this.schoolId = schoolId;
  }

  public String getSchoolName() {
    return schoolName;
  }

  public void setSchoolName(String schoolName) {
    this.schoolName = schoolName;
  }

  public List<UserGroupModel> getGroups() {
    return Collections.unmodifiableList(groups);
  }

  public void setGroups(
      List<UserGroupModel> groups) {
    this.groups = groups;
  }

  @Override
  public String getName() {
    return String.format("%s %s", firstName, lastName);
  }

  @Override
  public boolean hasPermission(String permission) {
    return permissions.contains(permission);
  }
}
