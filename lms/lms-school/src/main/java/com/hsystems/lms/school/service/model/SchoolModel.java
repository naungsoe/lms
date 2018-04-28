package com.hsystems.lms.school.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchoolModel implements Serializable {

  private static final long serialVersionUID = 11677610017568455L;

  private String id;

  private String name;

  private List<String> permissions;

  private PreferencesModel preferences;

  private UserModel createdBy;

  private String createdDateTime;

  private UserModel modifiedBy;

  private String modifiedDateTime;

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
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyList() : permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = new ArrayList<>();
    this.permissions.addAll(permissions);
  }

  public PreferencesModel getPreferences() {
    return preferences;
  }

  public void setPreferences(PreferencesModel preferences) {
    this.preferences = preferences;
  }

  public UserModel getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserModel createdBy) {
    this.createdBy = createdBy;
  }

  public String getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(String createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  public UserModel getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(UserModel modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public String getModifiedDateTime() {
    return modifiedDateTime;
  }

  public void setModifiedDateTime(String modifiedDateTime) {
    this.modifiedDateTime = modifiedDateTime;
  }
}