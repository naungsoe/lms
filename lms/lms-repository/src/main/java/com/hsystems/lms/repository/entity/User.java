package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
@IndexCollection(namespace = "lms", name = "users")
public final class User
    implements Entity, SchoolScoped, Auditable, Serializable {

  private static final long serialVersionUID = -4602729636176647790L;

  @IndexField
  private String id;

  @IndexField
  private String firstName;

  @IndexField
  private String lastName;

  @IndexField
  private String account;

  @IndexField
  private String password;

  @IndexField
  private String salt;

  @IndexField
  private LocalDateTime dateOfBirth;

  @IndexField
  private String gender;

  @IndexField
  private String mobile;

  @IndexField
  private String email;

  @IndexField
  private String locale;

  @IndexField
  private String timeFormat;

  @IndexField
  private String dateFormat;

  @IndexField
  private String dateTimeFormat;

  @IndexField
  private List<Permission> permissions;

  @IndexField
  private School school;

  @IndexField
  private User createdBy;

  @IndexField
  private LocalDateTime createdDateTime;

  @IndexField
  private User modifiedBy;

  @IndexField
  private LocalDateTime modifiedDateTime;

  User() {

  }

  User(
      String id,
      String firstName,
      String lastName,
      String account,
      String password,
      String salt,
      LocalDateTime dateOfBirth,
      String gender,
      String mobile,
      String email,
      String locale,
      String timeFormat,
      String dateFormat,
      String dateTimeFormat,
      List<Permission> permissions,
      School school,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.account = account;
    this.password = password;
    this.salt = salt;
    this.dateOfBirth = dateOfBirth;
    this.gender = gender;
    this.mobile = mobile;
    this.email = email;
    this.locale = locale;
    this.timeFormat = timeFormat;
    this.dateFormat = dateFormat;
    this.dateTimeFormat = dateTimeFormat;
    this.permissions = permissions;
    this.school = school;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  public static class Builder {

    private String id;
    private String firstName;
    private String lastName;

    private String account;
    private String password;
    private String salt;
    private LocalDateTime dateOfBirth;
    private String gender;
    private String mobile;
    private String email;
    private String locale;
    private String timeFormat;
    private String dateFormat;
    private String dateTimeFormat;
    private List<Permission> permissions;
    private School school;
    private User createdBy;
    private LocalDateTime createdDateTime;
    private User modifiedBy;
    private LocalDateTime modifiedDateTime;

    public Builder(String id, String firstName, String lastName) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
    }

    public Builder account(String account) {
      this.account = account;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder salt(String salt) {
      this.salt = salt;
      return this;
    }

    public Builder dateOfBirth(LocalDateTime dateOfBirth) {
      this.dateOfBirth = dateOfBirth;
      return this;
    }

    public Builder gender(String gender) {
      this.gender = gender;
      return this;
    }

    public Builder mobile(String mobile) {
      this.mobile = mobile;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder locale(String locale) {
      this.locale = locale;
      return this;
    }

    public Builder timeFormat(String timeFormat) {
      this.timeFormat = timeFormat;
      return this;
    }

    public Builder dateFormat(String dateFormat) {
      this.dateFormat = dateFormat;
      return this;
    }

    public Builder dateTimeFormat(String dateTimeFormat) {
      this.dateTimeFormat = dateTimeFormat;
      return this;
    }

    public Builder permissions(List<Permission> permissions) {
      this.permissions = permissions;
      return this;
    }

    public Builder school(School school) {
      this.school = school;
      return this;
    }

    public Builder createdBy(User createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    public Builder createdDateTime(LocalDateTime createdDateTime) {
      this.createdDateTime = createdDateTime;
      return this;
    }

    public Builder modifiedBy(User modifiedBy) {
      this.modifiedBy = modifiedBy;
      return this;
    }

    public Builder modifiedDateTime(LocalDateTime modifiedDateTime) {
      this.modifiedDateTime = modifiedDateTime;
      return this;
    }

    public User build() {
      return new User(
          this.id,
          this.firstName,
          this.lastName,
          this.account,
          this.password,
          this.salt,
          this.dateOfBirth,
          this.gender,
          this.mobile,
          this.email,
          this.locale,
          this.timeFormat,
          this.dateFormat,
          this.dateTimeFormat,
          this.permissions,
          this.school,
          this.createdBy,
          this.createdDateTime,
          this.modifiedBy,
          this.modifiedDateTime
      );
    }
  }

  @Override
  public String getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
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

  public String getTimeFormat() {
    return timeFormat;
  }

  public String getDateFormat() {
    return dateFormat;
  }

  public String getDateTimeFormat() {
    return dateTimeFormat;
  }

  public Enumeration<Permission> getPermissions() {
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(permissions);
  }

  @Override
  public School getSchool() {
    return school;
  }

  @Override
  public User getCreatedBy() {
    return createdBy;
  }

  @Override
  public LocalDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  @Override
  public User getModifiedBy() {
    return modifiedBy;
  }

  @Override
  public LocalDateTime getModifiedDateTime() {
    return modifiedDateTime;
  }

  @Override
  public String toString() {
    return String.format(
        "User{id=%s, firstName=%s, lastName=%s, account=%s, password=%s, "
            + "salt=%s, dateOfBirth=%s, gender=%s, mobile=%s, email=%s, "
            + "locale=%s, dateFormat=%s, timeFormat=%s, dateTimeFormat=%s, "
            + "permissions=%s, school=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, firstName, lastName, account, password, salt, dateOfBirth,
        gender, mobile, email, locale, timeFormat, dateFormat, dateTimeFormat,
        StringUtils.join(permissions, ","), school, createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
