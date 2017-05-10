package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.ListUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel
    extends AuditableModel implements Principal, Serializable {

  private static final long serialVersionUID = -8222871542135593721L;

  private String id;

  private String account;

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

  private SchoolModel school;

  private List<GroupModel> groups;

  UserModel() {

  }

  public UserModel(
      String id,
      String account,
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
      SchoolModel school,
      List<GroupModel> groups) {

    this.id = id;
    this.account = account;
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
    this.school = school;
    this.groups = groups;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
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

  @Override
  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  @Override
  public String getDateFormat() {
    return dateFormat;
  }

  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  @Override
  public String getDateTimeFormat() {
    return dateTimeFormat;
  }

  public void setDateTimeFormat(String dateTimeFormat) {
    this.dateTimeFormat = dateTimeFormat;
  }

  public List<String> getPermissions() {
    return ListUtils.isEmpty(permissions)
        ? Collections.emptyList()
        : Collections.unmodifiableList(permissions);
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = new ArrayList<>();
    this.permissions.addAll(permissions);
  }

  public SchoolModel getSchool() {
    return school;
  }

  public void setSchool(SchoolModel school) {
    this.school = school;
  }

  public List<GroupModel> getGroups() {
    return ListUtils.isEmpty(groups)
        ? Collections.emptyList()
        : Collections.unmodifiableList(groups);
  }

  public void setGroups(List<GroupModel> groups) {
    this.groups = new ArrayList<>();
    this.groups.addAll(groups);
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
