package com.hsystems.lms.user.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
public final class SchoolUser extends AppUser implements Serializable {

  private static final long serialVersionUID = -1333514444720632893L;

  private School school;

  private List<Group> groups;

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
      boolean mfaEnabled,
      School school,
      List<Group> groups) {

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
    this.mfaEnabled = mfaEnabled;
    this.school = school;
    this.groups = groups;
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
    private Credentials credentials;
    private boolean mfaEnabled;
    private School school;
    private List<Group> groups;

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

    public Builder credentials(Credentials credentials) {
      this.credentials = credentials;
      return this;
    }

    public Builder mfaEnabled(boolean mfaEnabled) {
      this.mfaEnabled = mfaEnabled;
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
          this.credentials,
          this.mfaEnabled,
          this.school,
          this.groups
      );
    }
  }

  public School getSchool() {
    return school;
  }

  public Enumeration<Group> getGroups() {
    return CollectionUtils.isEmpty(groups)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(groups);
  }
}