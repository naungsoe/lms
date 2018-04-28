package com.hsystems.lms.user.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
public final class SchoolUser implements User, Serializable {

  private static final long serialVersionUID = 4856432686379587211L;

  private String id;

  private String firstName;

  private String lastName;

  private LocalDateTime dateOfBirth;

  private String gender;

  private String mobile;

  private String email;

  private List<String> permissions;

  private Preferences preferences;

  private Credentials credentials;

  private School school;

  private List<Group> groups;

  private User createdBy;

  private LocalDateTime createdDateTime;

  private User modifiedBy;

  private LocalDateTime modifiedDateTime;

  SchoolUser() {

  }

  SchoolUser(
      String id,
      String firstName,
      String lastName,
      LocalDateTime dateOfBirth,
      String gender,
      String mobile,
      String email,
      List<String> permissions,
      Preferences preferences,
      Credentials credentials,
      School school,
      List<Group> groups,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.gender = gender;
    this.mobile = mobile;
    this.email = email;
    this.permissions = permissions;
    this.preferences = preferences;
    this.credentials = credentials;
    this.school = school;
    this.groups = groups;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  public static class Builder {

    private String id;
    private String firstName;
    private String lastName;

    private LocalDateTime dateOfBirth;
    private String gender;
    private String mobile;
    private String email;
    private List<String> permissions;
    private Preferences preferences;
    private School school;
    private List<Group> groups;
    private User createdBy;
    private LocalDateTime createdDateTime;
    private User modifiedBy;
    private LocalDateTime modifiedDateTime;

    public Builder(String id, String firstName, String lastName) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
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

    public Builder permissions(List<String> permissions) {
      this.permissions = permissions;
      return this;
    }

    public Builder preferences(Preferences preferences) {
      this.preferences = preferences;
      return this;
    }

    public Builder school(School school) {
      this.school = school;
      return this;
    }

    public Builder groups(List<Group> groups) {
      this.groups = groups;
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

    public SchoolUser build() {
      return new SchoolUser(
          this.id,
          this.firstName,
          this.lastName,
          this.dateOfBirth,
          this.gender,
          this.mobile,
          this.email,
          this.permissions,
          this.preferences,
          this.school,
          this.groups,
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

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
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

  public Enumeration<String> getPermissions() {
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(permissions);
  }

  public Preferences getPreferences() {
    return preferences;
  }

  public School getSchool() {
    return school;
  }

  public Enumeration<Group> getGroups() {
    return CollectionUtils.isEmpty(groups)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(groups);
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public LocalDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  public User getModifiedBy() {
    return modifiedBy;
  }

  public LocalDateTime getModifiedDateTime() {
    return modifiedDateTime;
  }
}