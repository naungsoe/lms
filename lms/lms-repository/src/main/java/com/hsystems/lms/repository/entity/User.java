package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
public class User extends Auditable implements Serializable {

  private static final long serialVersionUID = -3039577050861410422L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  private String password;

  private String salt;

  @IndexField(type = IndexFieldType.STRING)
  private String firstName;

  @IndexField(type = IndexFieldType.STRING)
  private String lastName;

  @IndexField(type = IndexFieldType.DATETIME)
  private LocalDateTime dateOfBirth;

  @IndexField(type = IndexFieldType.STRING)
  private String gender;

  @IndexField(type = IndexFieldType.STRING)
  private String mobile;

  @IndexField(type = IndexFieldType.STRING)
  private String email;

  private String locale;

  private String dateFormat;

  private String dateTimeFormat;

  private List<Permission> permissions;

  @IndexField(type = IndexFieldType.OBJECT)
  private School school;

  @IndexField(type = IndexFieldType.LIST)
  private List<Group> groups;

  User() {

  }

  public User(
      String id,
      String password,
      String salt,
      String firstName,
      String lastName,
      LocalDateTime dateOfBirth,
      String gender,
      String mobile,
      String email,
      String locale,
      String dateFormat,
      String dateTimeFormat,
      List<Permission> permissions,
      School school,
      List<Group> groups,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

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
    this.school = school;
    this.groups = groups;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
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

  public LocalDateTime getDateOfBirth() {
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

  public School getSchool() {
    return school;
  }

  public List<Group> getGroups() {
    return Collections.unmodifiableList(groups);
  }
}
