package com.hsystems.lms.level.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.school.service.model.SchoolModel;
import com.hsystems.lms.school.service.model.UserModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class LevelModel implements Serializable {

  private static final long serialVersionUID = 5182103918190020379L;

  private String id;

  private String name;

  private SchoolModel school;

  private UserModel createdBy;

  private String createdOn;

  private UserModel modifiedBy;

  private String modifiedOn;

  public LevelModel() {

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

  public SchoolModel getSchool() {
    return school;
  }

  public void setSchool(SchoolModel school) {
    this.school = school;
  }

  public UserModel getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserModel createdBy) {
    this.createdBy = createdBy;
  }

  public String getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
  }

  public UserModel getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(UserModel modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public String getModifiedOn() {
    return modifiedOn;
  }

  public void setModifiedOn(String modifiedOn) {
    this.modifiedOn = modifiedOn;
  }
}