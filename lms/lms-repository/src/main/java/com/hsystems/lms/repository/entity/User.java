package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
@IndexCollection(name = "users")
public class User extends Auditable implements Entity, Serializable {

  private static final long serialVersionUID = -3039577050861410422L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.STRING)
  private String account;

  @IndexField(type = IndexFieldType.STRING)
  private String password;

  @IndexField(type = IndexFieldType.STRING)
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

  @IndexField(type = IndexFieldType.STRING)
  private String locale;

  @IndexField(type = IndexFieldType.STRING)
  private String dateFormat;

  @IndexField(type = IndexFieldType.STRING)
  private String dateTimeFormat;

  @IndexField(type = IndexFieldType.LIST)
  private List<Permission> permissions;

  @IndexField(type = IndexFieldType.OBJECT)
  private School school;

  @IndexField(type = IndexFieldType.LIST)
  private List<Group> groups;

  User() {

  }

  public User(
      String id,
      String firstName,
      String lastName) {

    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(
      String id,
      String account,
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
    this.account = account;
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

  @Override
  public String getId() {
    return id;
  }

  public String getAccount() {
    return account;
  }

  public String getPassword() {
    return password;
  }

  public String getSalt() {
    return salt;
  }

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

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    User user = (User) obj;
    return id.equals(user.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "School{id=%s, account=%s, password=%s, salt=%s, firstName=%s, "
            + "lastName=%s, dateOfBirth=%s, gender=%s, "
            + "mobile=%s, email=%s, locale=%s, dateFormat=%s, "
            + "dateTimeFormat=%s, permissions=%s, school=%s, groups=%s, "
            + "createdBy=%s, createdDateTime=%s, modifiedBy=%s, "
            + "modifiedDateTime=%s}",
        id, account, password, salt, firstName, lastName, dateOfBirth,
        gender, mobile, email, locale, dateFormat, dateTimeFormat,
        StringUtils.join(permissions, ","), school,
        StringUtils.join(groups, ","), createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
