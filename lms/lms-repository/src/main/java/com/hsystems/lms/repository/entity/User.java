package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.annotation.Groups;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
public class User extends Auditable implements Entity, Serializable {

  private static final long serialVersionUID = -3039577050861410422L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.STRING)
  private String signInId;

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
      String signInId,
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
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.signInId = signInId;
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
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getSignInId() {
    return signInId;
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

  @Groups
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
    return signInId.equals(user.getSignInId());
  }

  @Override
  public String toString() {
    StringBuilder permissionsBuilder = new StringBuilder();
    permissions.forEach(permission
        -> permissionsBuilder.append(permission).append(","));

    return String.format(
        "School{id=%s, signInId=%s, password=%s, salt=%s, firstName=%s, "
            + "lastName=%s, dateOfBirth=%s, gender=%s, mobile=%s, "
            + "email=%s, locale=%s, dateFormat=%s, dateTimeFormat=%s, "
            + "permissions=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, signInId, password, salt, firstName, lastName, dateOfBirth, gender,
        mobile, email, locale, dateFormat, dateTimeFormat, permissionsBuilder,
        createdBy, createdDateTime, modifiedBy, modifiedDateTime);
  }
}
