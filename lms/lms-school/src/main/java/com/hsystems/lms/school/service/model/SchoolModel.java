package com.hsystems.lms.school.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchoolModel extends AuditableModel {

  private static final long serialVersionUID = -2187226140470382617L;

  private String id;

  private String name;

  private List<String> permissions;

  private PreferencesModel preferences;

  public SchoolModel() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public void setPreferences(PreferencesModel preferences) {
    this.preferences = preferences;
  }
}