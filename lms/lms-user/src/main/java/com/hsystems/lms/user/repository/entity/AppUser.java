package com.hsystems.lms.user.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.school.repository.entity.Preferences;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
public class AppUser implements User, Serializable {

  private static final long serialVersionUID = -6735250376050635406L;

  protected String id;

  protected String firstName;

  protected String lastName;

  protected LocalDateTime dateOfBirth;

  protected String gender;

  protected String mobile;

  protected String email;

  protected List<String> permissions;

  protected Preferences preferences;

  protected Credentials credentials;

  protected boolean mfaEnabled;

  AppUser() {

  }

  AppUser(
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
      boolean mfaEnabled) {

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

    public AppUser build() {
      return new AppUser(
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
          this.mfaEnabled
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

  public Credentials getCredentials() {
    return credentials;
  }

  public boolean isMfaEnabled() {
    return mfaEnabled;
  }
}