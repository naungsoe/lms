package com.hsystems.lms.user.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.school.service.model.AuditableModel;
import com.hsystems.lms.school.service.model.PreferencesModel;

import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppUserModel extends AuditableModel implements Principal {

  private static final long serialVersionUID = -2714661138072896059L;

  private String id;

  private String firstName;

  private String lastName;

  private String dateOfBirth;

  private String gender;

  private String mobile;

  private String email;

  private List<String> permissions;

  private PreferencesModel preferences;

  private CredentialsModel credentials;

  public AppUserModel() {

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

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public PreferencesModel getPreferences() {
    return preferences;
  }

  public void setPreferences(
      PreferencesModel preferences) {
    this.preferences = preferences;
  }

  public CredentialsModel getCredentials() {
    return credentials;
  }

  public void setCredentials(CredentialsModel credentials) {
    this.credentials = credentials;
  }

  @Override
  public boolean hasPermission(String permission) {
    return permissions.contains(permission);
  }
}